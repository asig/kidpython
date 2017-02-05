package com.asigner.kidpython.runtime;

import com.asigner.kidpython.compiler.ast.Node;
import com.asigner.kidpython.ide.util.AnsiEscapeCodes;
import com.asigner.kidpython.runtime.nativecode.Export;
import com.asigner.kidpython.util.Pair;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.lang.reflect.Method;
import java.math.MathContext;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.Stack;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static com.asigner.kidpython.runtime.Value.Type.BOOLEAN;
import static com.asigner.kidpython.runtime.Value.Type.ITERATOR;
import static com.asigner.kidpython.runtime.Value.Type.MAP;
import static com.asigner.kidpython.runtime.Value.Type.NUMBER;
import static com.asigner.kidpython.runtime.Value.Type.RANGE;
import static com.asigner.kidpython.runtime.Value.Type.REFERENCE;
import static com.asigner.kidpython.runtime.Value.Type.STRING;
import static com.asigner.kidpython.runtime.VirtualMachine.State.PAUSED;
import static com.asigner.kidpython.runtime.VirtualMachine.State.RUNNING;
import static com.asigner.kidpython.runtime.VirtualMachine.State.STOPPED;

public class VirtualMachine {

    private static final Logger logger = Logger.getLogger(VirtualMachine.class.getName());
    
    public interface EventListener {
        void vmStateChanged();
        void newStatementReached(Node stmt);
        void programSet();
        void reset();
        void enteringFunction(FuncValue func); // Only called for non-native functions
        void leavingFunction(); // Only called for non-native functions
    }

    public enum State {
        STOPPED,
        PAUSED,
        RUNNING
    }

    public static class Frame {
        private final Frame parent;
        private int returnAddress;
        private final Map<String, Value> vars;
        private final Map<String, VarType> types;

        public Frame(Frame parent, int returnAddress) {
            this.parent = parent;
            this.returnAddress = returnAddress;
            this.vars = Maps.newHashMap();
            this.types = Maps.newHashMap();
        }

        public Frame getParent() {
            return parent;
        }

        public int getReturnAddress() {
            return returnAddress;
        }

        public boolean isDefined(String name) {
            return vars.containsKey(name);
        }

        public Value getVar(String name) {
            return vars.get(name);
        }

        public void setVar(String name, VarType type, Value value) {
            vars.put(name, value);
            types.put(name, type);
        }

        public Set<String> getVarNames(Set<VarType> types) {
            return this.types.entrySet().stream()
                    .filter(e -> types.contains(e.getValue()))
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toSet());
        }
    }

    private State state;
    private Frame funcFrame;
    private Frame globalFrame;
    private Instruction[] program;
    private int pc;
    private Node lastStmt;

    private Lock stateLock = new ReentrantLock();
    private Condition stateChanged = stateLock.newCondition();


    private Stack<Value> valueStack = new Stack<>();

    private List<EventListener> listeners = Lists.newArrayList();

    private final PrintStream stdout;
    private final InputStream stdin;
    private final List<Object> nativeWrappers;

    private class ExecutorThread extends Thread {

        ExecutorThread() {
            setDaemon(true);
            setName("VM Executor");
        }

        @Override
        public void run() {
            for (; ; ) {
                try {
                    stateLock.lock();
                    stateChanged.await();
                } catch (InterruptedException ignored) {
                } finally {
                    stateLock.unlock();
                }
                try {
                    while (state == RUNNING) {
                        executeInstruction();
                    }
                } catch (Exception e) {
                    stdout.print(AnsiEscapeCodes.FG_RED);
                    stdout.println("Oooops, something went wrong...");
                    e.printStackTrace(stdout);
                    stdout.println("Stopping execution");
                    stdout.print(AnsiEscapeCodes.RESET);
                    VirtualMachine.this.stop();
                }
            }
        }

        private void executeInstruction() {
            Instruction instr = program[pc++];
            Node stmt = instr.getSourceNode();
            if (stmt != lastStmt) {
                lastStmt = stmt;
                cloneListeners().forEach(l -> l.newStatementReached(stmt));
            }
            if (logger.isLoggable(Level.FINE)) {
                logger.fine(String.format("Executing: %04d (Line %02d) %s", pc - 1, instr.getSourceNode().getPos().getLine(), instr));
            }
            switch (instr.getOpCode()) {
                case PUSH:
                    valueStack.push(instr.getVal());
                    break;

                case POP:
                    valueStack.pop();
                    break;

                case DUP: {
                    Value v = valueStack.peek();
                    valueStack.push(v);
                }
                break;

                case ASSIGN: {
                    Value rhs = load(valueStack.pop());
                    Value lhs = valueStack.pop();
                    if (lhs.getType() != REFERENCE) {
                        throw new ExecutionException(instr.getSourceNode().getPos(), "Can't assign to non-reference!");
                    }
                    if (lhs instanceof VarRefValue) {
                        VarRefValue vrv = (VarRefValue)lhs;
                        setVar(vrv.getVarName(), vrv.getVarType(), rhs);
                    } else if (lhs instanceof FieldRefValue) {
                        FieldRefValue frv = (FieldRefValue) lhs;
                        frv.getMap().asMap().put(frv.getKey(), rhs);
                    }

                }
                break;

                case STOP:
                    VirtualMachine.this.stop();
                    break;

                case MKFIELDREF: {
                    Value key = load(valueStack.pop());
                    Value mapRef = valueStack.pop();
                    Value mapValue = load(mapRef);
                    if (mapValue == null && mapRef instanceof VarRefValue) {
                        // Variable does not exist yet. Create it.
                        VarRefValue varRef = (VarRefValue) mapRef;
                        setVar(varRef.getVarName(), varRef.getVarType(), new MapValue(Maps.newHashMap()));
                        mapValue = load(mapRef);
                    }
                    if (mapValue.getType() != MAP) {
                        throw new ExecutionException(instr.getSourceNode().getPos(), "Variable is not a map");
                    }
                    valueStack.push(new FieldRefValue((MapValue) mapValue, key));
                }
                break;

                case MKLIST: {
                    int elemCount = instr.getIntVal();
                    List<Value> elems = Lists.newArrayListWithExpectedSize(elemCount);
                    for (int i = elemCount - 1; i >= 0; i--) {
                        elems.add(load(valueStack.pop()));
                    }
                    elems = Lists.reverse(elems);
                    valueStack.push(new ListValue(elems));
                }
                break;

                case MKMAP: {
                    Map<Value, Value> map = Maps.newHashMap();
                    int elemCount = instr.getIntVal();
                    for (int i = elemCount - 1; i >= 0; i--) {
                        Value val = load(valueStack.pop());
                        Value key = load(valueStack.pop());
                        map.put(key, val);
                    }
                    valueStack.push(new MapValue(map));
                }
                break;

                case MKITER: {
                    Value val = load(valueStack.pop());
                    valueStack.push(new IterValue(val.asIterator()));
                }
                break;

                case MKRANGE: {
                    Value end = load(valueStack.pop());
                    Value start = load(valueStack.pop());
                    valueStack.push(new RangeValue(start, end));
                }
                break;

                case ITER_NEXT: {
                    Value val = load(valueStack.pop());
                    if (val.getType() != ITERATOR) {
                        throw new ExecutionException(instr.getSourceNode().getPos(), "Not an iterator!");
                    }
                    IterValue ival = (IterValue) val;
                    valueStack.push(ival.getIterator().next());
                }
                break;

                case ITER_HAS_NEXT: {
                    Value val = load(valueStack.pop());
                    if (val.getType() != ITERATOR) {
                        throw new ExecutionException(instr.getSourceNode().getPos(), "Not an iterator!");
                    }
                    IterValue ival = (IterValue) val;
                    valueStack.push(new BooleanValue(ival.getIterator().hasNext()));
                }
                break;

                case BT: {
                    Value v = load(valueStack.pop());
                    if (v.asBool()) {
                        pc = instr.getIntVal();
                    }
                }
                break;

                case BF: {
                    Value v = load(valueStack.pop());
                    if (!v.asBool()) {
                        pc = instr.getIntVal();
                    }
                }
                break;

                case B:
                    pc = instr.getIntVal();
                    break;

                case CALL: {
                    int paramCount = instr.getIntVal();
                    List<Value> params = Lists.newArrayListWithExpectedSize(paramCount);
                    for (int i = paramCount - 1; i >= 0; i--) {
                        params.add(load(valueStack.pop()));
                    }
                    params = Lists.reverse(params);
                    Value func = load(valueStack.pop());
                    if (func instanceof NativeFuncValue) {
                        Value result = ((NativeFuncValue) func).getFunc().run(params);
                        valueStack.push(result);
                    } else if (func instanceof FuncValue) {
                        enterFunction((FuncValue) func, params);
                    } else {
                        throw new ExecutionException(instr.getSourceNode().getPos(), "Can't call non-function value");
                    }
                }

                break;

                case RET:
                    leaveFunction();
                    break;

                case NOT: {
                    Value val = load(valueStack.pop());
                    valueStack.push(new BooleanValue(!val.asBool()));
                }
                break;

                case NEG: {
                    Value val = load(valueStack.pop());
                    valueStack.push(new NumberValue(val.asNumber().negate()));
                }
                break;

                case ADD: {
                    Value right = load(valueStack.pop());
                    Value left = load(valueStack.pop());
                    valueStack.push(addValues(left, right));
                }
                break;

                case SUB: {
                    Value right = load(valueStack.pop());
                    Value left = load(valueStack.pop());
                    valueStack.push(subValues(left, right));
                }
                break;

                case MUL: {
                    Value right = load(valueStack.pop());
                    Value left = load(valueStack.pop());
                    valueStack.push(mulValues(left, right));
                }
                break;

                case DIV: {
                    Value right = load(valueStack.pop());
                    Value left = load(valueStack.pop());
                    valueStack.push(divValues(left, right));
                }
                break;

                case EQ: {
                    Value right = load(valueStack.pop());
                    Value left = load(valueStack.pop());
                    valueStack.push(new BooleanValue(left.equals(right)));
                }
                break;
                case NE: {
                    Value right = load(valueStack.pop());
                    Value left = load(valueStack.pop());
                    valueStack.push(new BooleanValue(!left.equals(right)));
                }
                break;
                case LE: {
                    Value right = load(valueStack.pop());
                    Value left = load(valueStack.pop());
                    Optional<Integer> res = compare(left, right);
                    valueStack.push(new BooleanValue(res.isPresent() && res.get() <= 0));
                }
                break;
                case LT: {
                    Value right = load(valueStack.pop());
                    Value left = load(valueStack.pop());
                    Optional<Integer> res = compare(left, right);
                    valueStack.push(new BooleanValue(res.isPresent() && res.get() < 0));
                }
                break;
                case GE: {
                    Value right = load(valueStack.pop());
                    Value left = load(valueStack.pop());
                    Optional<Integer> res = compare(left, right);
                    valueStack.push(new BooleanValue(res.isPresent() && res.get() >= 0));
                }
                break;
                case GT: {
                    Value right = load(valueStack.pop());
                    Value left = load(valueStack.pop());
                    Optional<Integer> res = compare(left, right);
                    valueStack.push(new BooleanValue(res.isPresent() && res.get() > 0));
                }
                break;
                case IN: {
                    Value right = load(valueStack.pop());
                    Value left = load(valueStack.pop());
                    if (right.getType() != RANGE) {
                        throw new ExecutionException(instr.getSourceNode().getPos(), "Value is not a range!");
                    }
                    RangeValue rv = (RangeValue)right;
                    valueStack.push(new BooleanValue(rv.contains(left)));
                }
                break;

                default:
                    throw new IllegalStateException("Executing unimplemented op-code " + instr.getOpCode());
            }
        }
    }

    public VirtualMachine(OutputStream stdout, InputStream stdin, List<Object> nativeWrappers) {
        this.nativeWrappers = ImmutableList.copyOf(nativeWrappers);
        this.stdout = new PrintStream(stdout);
        this.stdin = stdin;
        reset();
        Thread executor = new ExecutorThread();
        executor.start();
    }

    public void addListener(EventListener listener) {
        this.listeners.add(listener);
    }

    public void removeListener(EventListener listener) {
        this.listeners.remove(listener);
    }

    public void setProgram(List<Instruction> instrs) {
        reset();
        this.program = instrs.toArray(new Instruction[instrs.size()]);
        cloneListeners().forEach(EventListener::programSet);
    }

    public State getState() {
        return state;
    }

    private void setState(State newState) {
        try {
            stateLock.lock();
            if (newState != state) {
                state = newState;
                stateChanged.signalAll();
                cloneListeners().forEach(EventListener::vmStateChanged);
            }
        } finally {
            stateLock.unlock();
        }
    }

    private List<EventListener> cloneListeners() {
        return Lists.newArrayList(listeners);
    }

    public void reset() {
        setState(STOPPED);
        this.globalFrame = new Frame(null, 0);
        this.funcFrame = globalFrame;
        this.program = null;
        this.pc = 0;
        this.lastStmt = null;

        nativeWrappers.forEach(this::registerNativeWrapper);

        cloneListeners().forEach(EventListener::reset);
    }

    private void registerNativeWrapper(Object w) {
        List<Pair<String, NativeFuncValue>> methods = findExportedMethods(w);
        Export export = w.getClass().getAnnotation(Export.class);
        if (export != null) {
            Map<Value, Value> map = Maps.newHashMap();
            for (Pair<String, NativeFuncValue> p : methods) {
                map.put(new StringValue(p.getFirst()), p.getSecond());
            }

            String name = export.name();
            if (Strings.isNullOrEmpty(name)) {
                name = w.getClass().getName();
            }
            globalFrame.setVar(name, VarType.SYSTEM, new MapValue(map));
        } else {
            for (Pair<String, NativeFuncValue> p : methods) {
                globalFrame.setVar(p.getFirst(), VarType.SYSTEM, p.getSecond());
            }
        }
    }

    private List<Pair<String, NativeFuncValue>> findExportedMethods(Object w) {
        List<Pair<String, NativeFuncValue>> methods = Lists.newArrayList();
        for (Method m : w.getClass().getMethods()) {
            Export methodExport = m.getAnnotation(Export.class);
            if (methodExport != null) {
                String name = methodExport.name();
                if (Strings.isNullOrEmpty(name)) {
                    name = m.getName();
                }
                final String finalName = name;
                NativeFuncValue.Iface caller = vals -> {
                    try {
                        return (Value) m.invoke(w, vals);
                    } catch (Exception e) {
                        throw new ExecutionException("Can't call native function " + finalName + ", e = " + e.getMessage());
                    }
                };
                methods.add(Pair.of(name, new NativeFuncValue(caller)));
            }
        }
        return methods;
    }

    public void stop() {
        setState(STOPPED);
    }

    public void pause() {
        if (state == RUNNING) {
            setState(PAUSED);
        }
    }

    public void start() {
        if (state != STOPPED && state != PAUSED) {
            return;
        }
        setState(RUNNING);
    }

    public Instruction getCurrentInstruction() {
        return pc < program.length ? program[pc] : null;
    }

    public Frame getCurrentFrame() {
        return funcFrame;
    }

    private void enterFunction(FuncValue func, List<Value> paramValues) {
        cloneListeners().forEach(l -> l.enteringFunction(func));
        Frame frame = new Frame(funcFrame, pc);
        funcFrame = frame;
        List<String> params = func.getParams();
        for (int i = 0; i < params.size(); i++) {
            frame.setVar(params.get(i), VarType.PARAMETER, paramValues.get(i));
        }
        pc = func.getStartPc();
    }

    private void leaveFunction() {
        cloneListeners().forEach(EventListener::leavingFunction);
        pc = funcFrame.getReturnAddress();
        funcFrame = funcFrame.getParent();
    }

    public Value getVar(String name) {
        Value v;
        v = funcFrame.getVar(name);
        if (v != null) {
            return v;
        }
        v = globalFrame.getVar(name);
        return v;
    }

    public void setVar(String name, VarType type, Value value) {
        Frame targetFrame;
        if (funcFrame.isDefined(name)) {
            targetFrame = funcFrame;
        } else if (globalFrame.isDefined(name)) {
            targetFrame = globalFrame;
        } else {
            targetFrame = funcFrame;
        }
        targetFrame.setVar(name, type, value);
    }

    private Value load(Value value) {
        if (value.getType() == REFERENCE) {
            if (value instanceof VarRefValue) {
                value = getVar(((VarRefValue) value).getVarName());
                if (value == null) {
                    value = UndefinedValue.INSTANCE;
                }
            } else if (value instanceof FieldRefValue) {
                FieldRefValue frv = (FieldRefValue) value;
                value = frv.getMap().asMap().get(frv.getKey());
                if (value == null) {
                    value = UndefinedValue.INSTANCE;
                }
            }
        }
        return value;
    }

    private Value addValues(Value left, Value right) {
        Value.Type tl = left.getType();
        Value.Type tr = right.getType();

        if (tl == STRING || tr == STRING) {
            return new StringValue(left.asString() + right.asString());
        } else {
            return new NumberValue(left.asNumber().add(right.asNumber()));
        }
    }

    private Value subValues(Value left, Value right) {
        return new NumberValue(left.asNumber().subtract(right.asNumber()));
    }

    private Value mulValues(Value left, Value right) {
        Value.Type tl = left.getType();
        Value.Type tr = right.getType();

        if ((tl == STRING && (tr == BOOLEAN || tr == NUMBER))
                || (tr == STRING && (tl == BOOLEAN || tl == NUMBER))) {
            String s;
            int count;
            if (tl == STRING) {
                s = left.asString();
                count = right.asNumber().intValue();
            } else {
                s = right.asString();
                count = left.asNumber().intValue();
            }
            StringBuilder bldr = new StringBuilder();
            while (count-- > 0) {
                bldr.append(s);
            }
            return new StringValue(bldr.toString());
        }

        return new NumberValue(left.asNumber().multiply(right.asNumber()));
    }

    private Value divValues(Value left, Value right) {
        return new NumberValue(left.asNumber().divide(right.asNumber(), MathContext.DECIMAL128));
    }

    private Optional<Integer> compare(Value v1, Value v2) {
        if (v1.getType() == v2.getType()) {
            switch (v1.getType()) {
                case BOOLEAN:
                    return Optional.of(Boolean.valueOf(v1.asBool()).compareTo(v2.asBool()));
                case STRING:
                    return Optional.of(v1.asString().compareTo(v2.asString()));
                case NUMBER:
                    return Optional.of(v1.asNumber().compareTo(v2.asNumber()));
            }
        }
        return Optional.empty();
    }
}
