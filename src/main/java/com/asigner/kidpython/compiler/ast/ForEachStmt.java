// Copyright 2016 Andreas Signer. All rights reserved.

package com.asigner.kidpython.compiler.ast;

import com.asigner.kidpython.compiler.Position;
import com.asigner.kidpython.compiler.ast.expr.ExprNode;

public class ForEachStmt extends Stmt {

    private ExprNode ctrlVar;
    private ExprNode range;
    private Stmt body;

    public ForEachStmt(Position pos, ExprNode ctrlVar, ExprNode range, Stmt body) {
        super(pos);
        this.ctrlVar = ctrlVar;
        this.range = range;
        this.body = body;
    }

    public Stmt getBody() {
        return body;
    }

    public ExprNode getCtrlVar() {
        return ctrlVar;
    }

    public ExprNode getRange() {
        return range;
    }

    @Override
    public void accept(NodeVisitor visitor) {
        visitor.visit(this);
    }

}
