// Copyright 2016 Andreas Signer. All rights reserved.

package com.asigner.kidpython.compiler.runtime;


import com.asigner.kidpython.compiler.ast.Node;

public class Instruction {
    public enum OpCode {
        PUSH,  // Push a value to the stack
        STORE, // Store from stack to variable

        MAP_ACCESS, // Access a map property

        MAKE_LIST, // Create a list of n elements
        MAKE_MAP,

        JUMP_IF_TRUE,
        CALL,
        CALL_NATIVE,
        RETURN,

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
    private final Integer i;
    private final Value val;
    private final Node node;

    public Instruction(Node node, OpCode opCode, Value val) {
        this.node = node;
        this.i = null;
        this.opCode = opCode;
        this.val = val;
    }

    public Instruction(Node node, OpCode opCode, Integer i) {
        this.node = node;
        this.i = i;
        this.opCode = opCode;
        this.val = null;
    }
}
