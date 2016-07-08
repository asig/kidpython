// Copyright 2016 Andreas Signer. All rights reserved.

package com.asigner.kidpython.compiler.ast.expr;

import com.asigner.kidpython.compiler.Position;
import com.asigner.kidpython.compiler.ast.NodeVisitor;

public class BinOpNode extends ExprNode {

    public enum Op {
        ADD, SUB, MUL, DIV,
        AND, OR,
        EQ, NE, LE, LT, GE, GT
    };

    private final Op op;
    private final ExprNode left, right;

    public BinOpNode(Position pos, Op op, ExprNode left, ExprNode right) {
        super(pos);
        this.left = left;
        this.right = right;
        this.op = op;
    }

    public Op getOp() {
        return op;
    }

    public ExprNode getLeft() {
        return left;
    }

    public ExprNode getRight() {
        return right;
    }

    @Override
    public void accept(NodeVisitor visitor) {
        visitor.visit(this);
    }
}
