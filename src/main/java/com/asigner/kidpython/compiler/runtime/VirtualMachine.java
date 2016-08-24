package com.asigner.kidpython.compiler.runtime;

import com.asigner.kidpython.compiler.ast.Node;
import com.asigner.kidpython.compiler.runtime.nativecode.MathWrapper;
import com.asigner.kidpython.compiler.runtime.nativecode.TurtleWrapper;
import com.asigner.kidpython.compiler.runtime.nativecode.UtilsWrapper;
import com.asigner.kidpython.ide.util.AnsiEscapeCodes;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Stack;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static com.asigner.kidpython.compiler.runtime.Value.Type.BOOLEAN;
import static com.asigner.kidpython.compiler.runtime.Value.Type.ITERATOR;
import static com.asigner.kidpython.compiler.runtime.Value.Type.MAP;
import static com.asigner.kidpython.compiler.runtime.Value.Type.NUMBER;
import static com.asigner.kidpython.compiler.runtime.Value.Type.REFERENCE;
import static com.asigner.kidpython.compiler.runtime.Value.Type.STRING;
import static com.asigner.kidpython.compiler.runtime.VirtualMachine.State.PAUSED;
import static com.asigner.kidpython.compiler.runtime.VirtualMachine.State.RUNNING;
import static com.asigner.kidpython.compiler.runtime.VirtualMachine.State.STOPPED;

public class VirtualMachine {

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

        public Frame(Frame parent, int returnAddress) {
            this.parent = parent;
            this.returnAddress = returnAddress;
            this.vars = Maps.newHashMap();
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

        public void setVar(String name, Value value) {
            vars.put(name, value);
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
    private final TurtleWrapper turtleWrapper;
    private final MathWrapper mathWrapper;
    private final UtilsWrapper utilsWrapper;

    class ExecutorThread extends Thread {

        public ExecutorThread() {
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
                } catch (Error e) {
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
                cloneListeners().stream().forEach(l -> l.newStatementReached(stmt));
            }
            System.err.println(String.format("Executing: %04d %s", pc - 1, instr));
            switch (instr.getOpCode()) {
                case PUSH:
                    valueStack.push(instr.getVal());
                    break;

                case POP:
                    valueStack.pop();
                    break;

                case ASSIGN: {
                    Value rhs = load(valueStack.pop());
                    Value lhs = valueStack.pop();
                    if (lhs.getType() != REFERENCE) {
                        throw new ExecutionException(instr.getSourceNode().getPos(), "Can't assign to non-reference!");
                    }
                    if (lhs instanceof VarRefValue) {
                        setVar(((VarRefValue) lhs).getVar(), rhs);
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
                        setVar(varRef.getVar(), new MapValue(Maps.newHashMap()));
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
            }
        }
    }

    public VirtualMachine(OutputStream stdout, InputStream stdin, TurtleWrapper turtleWrapper, MathWrapper mathWrapper, UtilsWrapper utilsWrapper) {
        this.turtleWrapper = turtleWrapper;
        this.mathWrapper = mathWrapper;
        this.utilsWrapper = utilsWrapper;
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
        cloneListeners().stream().forEach(EventListener::programSet);
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
                cloneListeners().stream().forEach(EventListener::vmStateChanged);
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
        this.funcFrame = null;
        this.globalFrame = new Frame(null, 0);
        this.program = null;
        this.pc = 0;
        this.lastStmt = null;

        utilsWrapper.registerWith(globalFrame);
        turtleWrapper.registerWith(globalFrame);
        mathWrapper.registerWith(globalFrame);

        cloneListeners().stream().forEach(EventListener::reset);
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
        return program[pc];
    }

    private void enterFunction(FuncValue func, List<Value> paramValues) {
        cloneListeners().stream().forEach(l -> l.enteringFunction(func));
        Frame frame = new Frame(funcFrame, pc);
        funcFrame = frame;
        List<String> params = func.getParams();
        for (int i = 0; i < params.size(); i++) {
            frame.setVar(params.get(i), paramValues.get(i));
        }
        pc = func.getStartPc();
    }

    private void leaveFunction() {
        cloneListeners().stream().forEach(EventListener::leavingFunction);
        pc = funcFrame.getReturnAddress();
        funcFrame = funcFrame.getParent();
    }

    public Value getVar(String name) {
        Value v;
        if (funcFrame != null) {
            v = funcFrame.getVar(name);
            if (v != null) {
                return v;
            }
        }
        v = globalFrame.getVar(name);
        return v;
    }

    public void setVar(String name, Value value) {
        Frame targetFrame;
        if (funcFrame != null) {
            if (funcFrame.isDefined(name)) {
                targetFrame = funcFrame;
            } else if (globalFrame.isDefined(name)) {
                targetFrame = globalFrame;
            } else {
                targetFrame = funcFrame;
            }
        } else {
            targetFrame = globalFrame;
        }
        targetFrame.setVar(name, value);
    }

    private Value load(Value value) {
        if (value.getType() == Value.Type.REFERENCE) {
            if (value instanceof VarRefValue) {
                value = getVar(((VarRefValue) value).getVar());
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
        return new NumberValue(left.asNumber().divide(right.asNumber(), BigDecimal.ROUND_HALF_DOWN));
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
