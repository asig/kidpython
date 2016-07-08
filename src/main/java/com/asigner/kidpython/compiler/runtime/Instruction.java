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

        BRANCH_IF_TRUE,
        BRANCH,
        CALL,
        CALL_NATIVE,
        RETURN,

        AND,
        OR,
        NOT,

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
    private final Node sourceNode;

    public Instruction(Node sourceNode, OpCode opCode, Value val) {
        this(sourceNode, opCode, null, val);
    }

    public Instruction(Node sourceNode, OpCode opCode, Integer i) {
        this(sourceNode, opCode, i, null);
    }

    public Instruction(Node sourceNode, OpCode opCode) {
        this(sourceNode, opCode, null, null);
    }

    private Instruction(Node sourceNode, OpCode opCode, Integer i, Value v) {
        this.sourceNode = sourceNode;
        this.opCode = opCode;
        this.i = null;
        this.val = v;
    }
}
