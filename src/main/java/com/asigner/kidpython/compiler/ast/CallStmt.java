// Copyright 2016 Andreas Signer. All rights reserved.

package com.asigner.kidpython.compiler.ast;

import com.asigner.kidpython.compiler.Position;
import com.asigner.kidpython.compiler.ast.expr.ExprNode;
import com.asigner.kidpython.compiler.runtime.FuncValue;
import com.asigner.kidpython.compiler.runtime.Value;

public class CallStmt extends Stmt {

    private ExprNode expr;
    private Stmt trueBranch;

    public CallStmt(Position pos, ExprNode expr) {
        super(pos);
        this.expr = expr;
    }

    @Override
    Stmt execute() {
        Value val = expr.eval();
        if (val.getType() != Value.Type.FUNCTION) {
            // Emit error: Can't call non-function
        }
        return ((FuncValue)val).getValue();
    }
}
