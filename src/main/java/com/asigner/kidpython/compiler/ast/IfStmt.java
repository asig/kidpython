// Copyright 2016 Andreas Signer. All rights reserved.

package com.asigner.kidpython.compiler.ast;

import com.asigner.kidpython.compiler.Position;
import com.asigner.kidpython.compiler.ast.expr.ExprNode;

public class IfStmt extends Stmt {

    private ExprNode cond;
    private Stmt trueBranch;

    public IfStmt(Position pos, ExprNode cond, Stmt trueBranch) {
        super(pos);
        this.cond = cond;
        this.trueBranch = trueBranch;
    }

    @Override
    public Stmt execute() {
        if (cond.eval().asBool()) {
            return trueBranch;
        } else {
            return next;
        }
    }

    public Stmt getTrueBranch() {
        return trueBranch;
    }

    @Override
    public void accept(StmtVisitor visitor) {
        visitor.visit(this);
    }

}
