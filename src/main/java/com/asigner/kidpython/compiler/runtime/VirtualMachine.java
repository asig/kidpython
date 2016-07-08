package com.asigner.kidpython.compiler.runtime;

import com.google.common.collect.Maps;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class VirtualMachine {

    private static class Frame {
        private final Frame parent;
        private int returnAddress;
        private final Map<String, Value> vars;

        public Frame(Frame parent, int returnAddress) {
            this.parent = parent;
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

    private Frame funcFrame;
    private Frame globalFrame;
    private Instruction[] program;
    private int pc;

    private Stack<Value> valueStack = new Stack<>();

    private final PrintStream stdout;
    private final InputStream stdin;

    public VirtualMachine(OutputStream stdout, InputStream stdin) {
        this.stdout = new PrintStream(stdout);
        this.stdin = stdin;
        reset();
    }

    public void setProgram(List<Instruction> instrs) {
        this.program = instrs.toArray(new Instruction[instrs.size()]);
        this.pc = 0;
    }

    public void reset() {
        this.funcFrame = null;
        this.globalFrame = new Frame(null, 0);
        this.program = null;
        this.pc = 0;

        globalFrame.setVar("print", new NativeFuncValue(this::print));
    }

    public void run() {
        boolean running = true;
        while(running) {
            Instruction instr = program[pc++];
            switch(instr.getOpCode()) {
                case PUSH:
                    valueStack.push(instr.getVal());
                    break;

                case POP:
                    valueStack.pop();
                    break;

                case ASSIGN:
                    break;

                case STOP:
                    running = false;
                    break;

                case MAPACCESS:

                    break;

                case MKLIST:
                    break;

                case MKMAP:
                    break;

                case MKITER:
                    break;

                case BT: {
                    Value v = valueStack.pop();
                    if (v.asBool()) {
                        pc = instr.getIntVal();
                    }
                }
                break;

                case BF: {
                    Value v = valueStack.pop();
                    if (!v.asBool()) {
                        pc = instr.getIntVal();
                    }
                }
                break;

                case B:
                    pc = instr.getIntVal();
                    break;

                case CALL:
                    break;

                case CALLN:
                    break;

                case RET:
                    break;

                case NOT:
                    break;

                case NEG:
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

                case EQ:
                    break;
                case NE:
                    break;
                case LE:
                    break;
                case LT:
                    break;
                case GE:
                    break;
                case GT:
                    break;
            }
        }
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
        Frame targetFrame ;
        if (globalFrame.isDefined(name)) {
            targetFrame = globalFrame;
        } else {
            targetFrame = funcFrame != null ? funcFrame : globalFrame;
        }
        targetFrame.setVar(name, value);
    }

    private Value print(List<Value> values) {
        for (Value v : values) {
            stdout.print(v.asString());
        }
        return null;
    }

    private Value load(Value value) {
        if (value.getType() == Value.Type.REFERENCE) {
            ReferenceValue rValue = (ReferenceValue)value;
            value = getVar(rValue.getVar().getVar());
        }
        return value;
    }

    private Value addValues(Value left, Value right) {
        Value.Type tl = left.getType();
        Value.Type tr = right.getType();

        if (tl == Value.Type.STRING && tr == Value.Type.STRING) {
            return new StringValue(left.asString() + right.asString());
        } else {
            return new NumberValue(left.asNumber().add(right.asNumber()));
        }
    }

    private Value subValues(Value left, Value right) {
        return new NumberValue(left.asNumber().add(right.asNumber()));
    }

    private Value mulValues(Value left, Value right) {
        Value.Type tl = left.getType();
        Value.Type tr = right.getType();

        if (tl)

        return null;
    }

    private Value divValues(Value left, Value right) {
        return null;
    }

}
