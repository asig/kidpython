// Copyright 2016 Andreas Signer. All rights reserved.

package com.asigner.kidpython.compiler.ast;

import com.asigner.kidpython.compiler.Position;
import com.asigner.kidpython.compiler.ast.expr.ExprNode;

public class ForStmt extends Stmt {

    private final ExprNode ctrlVar;
    private final ExprNode start;
    private final ExprNode end;
    private final ExprNode step;
    private final Stmt body;

    public ForStmt(Position pos, ExprNode ctrlVar, ExprNode start, ExprNode end, ExprNode step, Stmt body) {
        super(pos);
        this.ctrlVar = ctrlVar;
        this.start = start;
        this.end = end;
        this.step = step;
        this.body = body;
    }

    public ExprNode getCtrlVar() {
        return ctrlVar;
    }

    public ExprNode getStart() {
        return start;
    }

    public ExprNode getEnd() {
        return end;
    }

    public ExprNode getStep() {
        return step;
    }

    public Stmt getBody() {
        return body;
    }

    @Override
    public void accept(NodeVisitor visitor) {
        visitor.visit(this);
    }

}
