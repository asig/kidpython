// Copyright 2016 Andreas Signer. All rights reserved.

package com.asigner.kidpython.compiler.ast.expr;

import com.asigner.kidpython.compiler.Position;
import com.asigner.kidpython.compiler.ast.Stmt;
import com.asigner.kidpython.compiler.runtime.FuncValue;
import com.asigner.kidpython.compiler.runtime.Value;

import java.util.List;

public class MakeFuncNode extends ExprNode {

    private final Stmt body;
    private final List<String> params;

    public MakeFuncNode(Position pos, Stmt body, List<String> params) {
        super(pos);
        this.body = body;
        this.params = params;
    }

    public Value eval() {
        return new FuncValue(body, params);
    }

    @Override
    void accept(ExprNodeVisitor visitor) {
        visitor.visit(this);

    }
}

