// Copyright 2016 Andreas Signer. All rights reserved.

package com.asigner.kidpython.compiler.ast;

import com.asigner.kidpython.compiler.Position;
import com.asigner.kidpython.compiler.ast.expr.ExprNode;

public class ForStmt extends Stmt {

    private ExprNode ctrlVar;
    private ExprNode start;
    private ExprNode end;
    private ExprNode step;
    private Stmt body;

    public ForStmt(Position pos, ExprNode ctrlVar, ExprNode start, ExprNode end, ExprNode step, Stmt body) {
        super(pos);
        this.ctrlVar = ctrlVar;
        this.start = start;
        this.end = end;
        this.step = step;
        this.body = body;
    }

    @Override
    public void accept(NodeVisitor visitor) {
        visitor.visit(this);
    }

}
