// Copyright 2016 Andreas Signer. All rights reserved.

package com.asigner.kidpython.compiler.ast;

import com.asigner.kidpython.compiler.Position;
import com.asigner.kidpython.compiler.ast.expr.ExprNode;

public class ReturnStmt extends Stmt {

    private ExprNode expr;

    public ReturnStmt(Position pos, ExprNode expr) {
        super(pos);
        this.expr = expr;
    }

    public ExprNode getExpr() {
        return expr;
    }

    @Override
    public Stmt execute() {
        expr.eval();
        return null;
    }

    @Override
    public void accept(StmtVisitor visitor) {
        visitor.visit(this);
    }
}
