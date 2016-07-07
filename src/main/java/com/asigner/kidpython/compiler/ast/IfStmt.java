// Copyright 2016 Andreas Signer. All rights reserved.

package com.asigner.kidpython.compiler.ast;

import com.asigner.kidpython.compiler.Position;
import com.asigner.kidpython.compiler.ast.expr.ExprNode;

public class IfStmt extends Stmt {

    private ExprNode cond;
    private Stmt trueBranch;
    private Stmt falseBranch;

    public IfStmt(Position pos, ExprNode cond, Stmt trueBranch) {
        super(pos);
        this.cond = cond;
        this.trueBranch = trueBranch;
    }

    public ExprNode getCond() {
        return cond;
    }

    public Stmt getFalseBranch() {
        return falseBranch;
    }

    public Stmt getTrueBranch() {
        return trueBranch;
    }

    public void setCond(ExprNode cond) {
        this.cond = cond;
    }

    public void setFalseBranch(Stmt falseBranch) {
        this.falseBranch = falseBranch;
    }

    public void setTrueBranch(Stmt trueBranch) {
        this.trueBranch = trueBranch;
    }

    @Override
    public void accept(NodeVisitor visitor) {
        visitor.visit(this);
    }

}
