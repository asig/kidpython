// Copyright 2016 Andreas Signer. All rights reserved.

package com.asigner.kidpython.compiler.ast;

import com.asigner.kidpython.compiler.Position;
import com.asigner.kidpython.compiler.ast.expr.Assignable;
import com.asigner.kidpython.compiler.ast.expr.ExprNode;
import com.asigner.kidpython.compiler.runtime.Environment;
import com.asigner.kidpython.compiler.runtime.ExecutionException;
import com.asigner.kidpython.compiler.runtime.Value;

public class AssignmentStmt extends Stmt {

    private ExprNode varExpr;
    private final ExprNode expr;

    public AssignmentStmt(Position pos, ExprNode varExpr, ExprNode expr) {
        super(pos);
        this.expr = expr;
        if (!(varExpr instanceof Assignable)) {
            throw new ExecutionException("Can't assign to this expression");
        }
        this.varExpr = varExpr;
    }

    @Override
    public Stmt execute(Environment env) {
        Value val = expr.eval(env);
        ((Assignable)varExpr).assign(env, val);
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
