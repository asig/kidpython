// Copyright 2016 Andreas Signer. All rights reserved.

package com.asigner.kidpython.compiler.runtime;


import com.asigner.kidpython.compiler.ast.Node;
import com.google.common.collect.Lists;

import java.util.List;

import static java.util.stream.Collectors.joining;

public class Instruction {
    public enum OpCode {
        PUSH,  // Push a value to the stack
        POP,
        ASSIGN, // Store from stack to variable
        STOP,

        MKFIELDREF,

        MKLIST,
        MKMAP,
        MKITER,

        BT,
        BF,
        B,
        CALL,
        RET,

        NOT,

        NEG,

        ADD,
        SUB,
        MUL,
        DIV,

        EQ,
        NE,
        LE,
        LT,
        GE,
        GT
    }

    private final OpCode opCode;
    private final Integer intVal;
    private final Value val;
    private final Node sourceNode;

    public Instruction(Node sourceNode, OpCode opCode, Value val) {
        this(sourceNode, opCode, null, val);
    }

    public Instruction(Node sourceNode, OpCode opCode, Integer intVal) {
        this(sourceNode, opCode, intVal, null);
    }

    public Instruction(Node sourceNode, OpCode opCode) {
        this(sourceNode, opCode, null, null);
    }

    private Instruction(Node sourceNode, OpCode opCode, Integer intVal, Value val) {
        this.sourceNode = sourceNode;
        this.opCode = opCode;
        this.intVal = intVal;
        this.val = val;
    }

    public Integer getIntVal() {
        return intVal;
    }

    public OpCode getOpCode() {
        return opCode;
    }

    public Node getSourceNode() {
        return sourceNode;
    }

    public Value getVal() {
        return val;
    }

    public String toString() {
        List<String> params = Lists.newArrayList();
        if (val != null) {
            params.add(val.toString());
        }
        if (intVal != null) {
            params.add(intVal.toString());
        }
        return String.format("%-9s %s", opCode.toString(), params.stream().collect(joining(",")));
    }
}
