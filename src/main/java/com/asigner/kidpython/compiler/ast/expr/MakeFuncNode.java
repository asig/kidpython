// Copyright 2016 Andreas Signer. All rights reserved.

package com.asigner.kidpython.compiler.ast.expr;

import com.asigner.kidpython.compiler.Position;
import com.asigner.kidpython.compiler.ast.NodeVisitor;
import com.asigner.kidpython.compiler.ast.Stmt;

import java.util.List;

public class MakeFuncNode extends ExprNode {

    private final Stmt body;
    private final List<String> params;

    public MakeFuncNode(Position pos, Stmt body, List<String> params) {
        super(pos);
        this.body = body;
        this.params = params;
    }

    public Stmt getBody() {
        return body;
    }

    public List<String> getParams() {
        return params;
    }

    @Override
    public void accept(NodeVisitor visitor) {
        visitor.visit(this);
    }
}

