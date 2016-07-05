// Copyright 2016 Andreas Signer. All rights reserved.

package com.asigner.kidpython.compiler.ast;

import com.asigner.kidpython.compiler.Position;
import com.asigner.kidpython.compiler.ast.expr.ExprNode;
import com.asigner.kidpython.compiler.runtime.Value;

public class AssignmentStmt extends Stmt {

    private ExprNode varExpr;
    private final ExprNode expr;

    public AssignmentStmt(Position pos, ExprNode varExpr, ExprNode expr) {
        super(pos);
        this.expr = expr;
        this.varExpr = varExpr;
    }

    @Override
    public Stmt execute() {
        Value val = expr.eval();
        // TODO(asigner); Assign value to ident
        return getNext();
    }

    @Override
    public void accept(StmtVisitor visitor) {
        visitor.visit(this);
    }

    public ExprNode getExpr() {
        return expr;
    }

    public ExprNode getVar() {
        return varExpr;
    }
}
