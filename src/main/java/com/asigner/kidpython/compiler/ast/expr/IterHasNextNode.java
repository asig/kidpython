// Copyright 2016 Andreas Signer. All rights reserved.

package com.asigner.kidpython.compiler.ast.expr;

import com.asigner.kidpython.compiler.Position;
import com.asigner.kidpython.compiler.ast.NodeVisitor;
import com.asigner.kidpython.compiler.runtime.BooleanValue;
import com.asigner.kidpython.compiler.runtime.Environment;
import com.asigner.kidpython.compiler.runtime.Value;
import com.google.common.base.Preconditions;

public class IterHasNextNode extends ExprNode {

    private final ExprNode expr;

    public IterHasNextNode(Position pos, ExprNode expr) {
        super(pos);
        this.expr = expr;
    }

    public ExprNode getExpr() {
        return expr;
    }

    @Override
    public void accept(NodeVisitor visitor) {
        visitor.visit(this);
    }
}
