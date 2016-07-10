// Copyright 2016 Andreas Signer. All rights reserved.

package com.asigner.kidpython.compiler.ast.expr;

import com.asigner.kidpython.compiler.Position;
import com.asigner.kidpython.compiler.ast.NodeVisitor;

public class VarNode extends ExprNode {

    private final String var;

    public VarNode(Position pos, String var) {
        super(pos);
        this.var = var;
    }

    public String getVar() {
        return var;
    }

    @Override
    public void accept(NodeVisitor visitor) {
        visitor.visit(this);
    }
}
