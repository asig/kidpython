// Copyright 2016 Andreas Signer. All rights reserved.

package com.asigner.kidpython.compiler.ast.expr;

import com.asigner.kidpython.compiler.Position;
import com.asigner.kidpython.compiler.ast.NodeVisitor;

public class RelOpNode extends BinaryNode {

    public enum Op {
        EQ, NE, LE, LT, GE, GT
    }

    private final Op op;

    public RelOpNode(Position pos, Op op, ExprNode left, ExprNode right) {
        super(pos, left, right);
        this.op = op;
    }

    public Op getOp() {
        return op;
    }

    @Override
    public void accept(NodeVisitor visitor) {
        visitor.visit(this);
    }
}
