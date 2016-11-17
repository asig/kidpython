// Copyright 2016 Andreas Signer. All rights reserved.

package com.asigner.kidpython.compiler.ast.expr;

import com.asigner.kidpython.compiler.Position;
import com.asigner.kidpython.compiler.ast.NodeVisitor;
import com.asigner.kidpython.runtime.VarType;

public class VarNode extends ExprNode {

    private final String varName;
    private final VarType varType;

    public VarNode(Position pos, String varName, VarType varType) {
        super(pos);
        this.varName = varName;
        this.varType = varType;
    }

    public String getVarName() {
        return varName;
    }

    public VarType getVarType() {
        return varType;
    }

    @Override
    public void accept(NodeVisitor visitor) {
        visitor.visit(this);
    }
}
