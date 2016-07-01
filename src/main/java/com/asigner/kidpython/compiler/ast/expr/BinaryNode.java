// Copyright 2016 Andreas Signer. All rights reserved.

package com.asigner.kidpython.compiler.ast.expr;

import com.asigner.kidpython.compiler.Position;

public abstract class BinaryNode extends ExprNode {

    protected final ExprNode left, right;

    public BinaryNode(Position pos, ExprNode left, ExprNode right) {
        super(pos);
        this.left = left;
        this.right = right;
    }
}
