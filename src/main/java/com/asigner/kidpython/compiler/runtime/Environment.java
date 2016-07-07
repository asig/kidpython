package com.asigner.kidpython.compiler.runtime;

import com.asigner.kidpython.compiler.ast.Stmt;
import com.google.common.collect.Maps;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class Environment {

    private static class Frame {
        private final Frame parent;
        private final Map<String, Value> vars;

        public Frame(Frame parent) {
            this.parent = parent;
            this.vars = Maps.newHashMap();
        }

        public Frame getParent() {
            return parent;
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

    private Stack<Value> stack = new Stack<>();

    private final PrintStream stdout;
    private final InputStream stdin;

    public Environment(OutputStream stdout, InputStream stdin) {
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
        this.globalFrame = new Frame(null);
        this.program = null;
        this.pc = 0;

        globalFrame.setVar("print", new NativeFuncValue(this::print));
    }

    public void run() {
        for(;;) {
            Instruction i = program[pc++];
            // Handle instr
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
}
