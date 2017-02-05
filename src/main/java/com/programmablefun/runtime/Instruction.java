/*
 * Copyright (c) 2017 Andreas Signer <asigner@gmail.com>
 *
 * This file is part of programmablefun.
 *
 * programmablefun is free software: you can redistribute it and/or
 * modify it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * programmablefun is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with programmablefun.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.programmablefun.runtime;


import com.programmablefun.compiler.ast.Node;
import com.google.common.collect.Lists;

import java.util.List;

import static java.util.stream.Collectors.joining;

public class Instruction {
    public enum OpCode {
        DUP,   // Duplicates the top value on the stack
        PUSH,  // Push a value to the stack
        POP,
        ASSIGN, // Store from stack to variable
        STOP,

        MKFIELDREF,

        MKLIST,
        MKMAP,
        MKITER,  // Create an iterator for the top value on the stack
        MKRANGE, // Create a range from the top 2 values on the stack

        BT,
        BF,
        B,
        CALL,
        RET,

        NOT,

        ITER_NEXT,
        ITER_HAS_NEXT,

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
        GT,
        IN, // check whether a value is within a range.
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
