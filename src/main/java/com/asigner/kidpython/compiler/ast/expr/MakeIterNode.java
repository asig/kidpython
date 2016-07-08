// Copyright 2016 Andreas Signer. All rights reserved.

package com.asigner.kidpython.compiler.ast.expr;

import com.asigner.kidpython.compiler.Position;
import com.asigner.kidpython.compiler.ast.NodeVisitor;

public class MakeIterNode extends ExprNode {

    private final ExprNode node;

    public MakeIterNode(Position pos, ExprNode node) {
        super(pos);
        this.node = node;
    }

    public ExprNode getNode() {
        return node;
    }

    @Override
    public void accept(NodeVisitor visitor) {
        visitor.visit(this);
    }
}
