// Copyright 2016 Andreas Signer. All rights reserved.

package com.asigner.kidpython.compiler.ast.expr;

import com.asigner.kidpython.compiler.Position;
import com.asigner.kidpython.compiler.runtime.Value;

public class ArithOpNode extends BinaryNode {

    public enum Op { ADD, SUB, MUL, DIV }

    private final Op op;

    public ArithOpNode(Position pos, Op op, ExprNode left, ExprNode right) {
        super(pos, left, right);
        this.op = op;
    }

    public Op getOp() {
        return op;
    }

    @Override
    public Value eval() {
        return null;
    }

    @Override
    void accept(ExprNodeVisitor visitor) {
        visitor.visit(this);

    }
}
