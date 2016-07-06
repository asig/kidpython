// Copyright 2016 Andreas Signer. All rights reserved.

package com.asigner.kidpython.compiler.ast;

import com.asigner.kidpython.compiler.Position;
import com.asigner.kidpython.compiler.ast.expr.ExprNode;
import com.asigner.kidpython.compiler.runtime.Environment;
import com.asigner.kidpython.compiler.runtime.FuncValue;
import com.asigner.kidpython.compiler.runtime.Value;

public class CallStmt extends Stmt {

    private ExprNode expr;

    public CallStmt(Position pos, ExprNode expr) {
        super(pos);
        this.expr = expr;
    }

    @Override
    public Stmt execute(Environment env) {
        Value val = expr.eval(env);
        if (val.getType() != Value.Type.FUNCTION) {
            // Emit error: Can't call non-function
        }
        return ((FuncValue)val).getValue();
    }

    public ExprNode getExpr() {
        return expr;
    }

    @Override
    public void accept(StmtVisitor visitor) {
        visitor.visit(this);
    }
}
