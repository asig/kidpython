// Copyright 2016 Andreas Signer. All rights reserved.

package com.asigner.kidpython.compiler.ast;

import com.asigner.kidpython.compiler.Position;
import com.asigner.kidpython.compiler.ast.expr.ExprNode;

public class ReturnStmt extends Stmt {

    private ExprNode expr;
    private Stmt trueBranch;

    public ReturnStmt(Position pos, ExprNode expr) {
        super(pos);
        this.expr = expr;
    }

    @Override
    Stmt execute() {
        expr.eval();
        return null;
    }
}
