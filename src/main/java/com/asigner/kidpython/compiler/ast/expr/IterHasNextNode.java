// Copyright 2016 Andreas Signer. All rights reserved.

package com.asigner.kidpython.compiler.ast.expr;

import com.asigner.kidpython.compiler.Position;
import com.asigner.kidpython.compiler.runtime.BooleanValue;
import com.asigner.kidpython.compiler.runtime.IterValue;
import com.asigner.kidpython.compiler.runtime.Value;
import com.google.common.base.Preconditions;

import java.math.BigDecimal;

public class IterHasNextNode extends ExprNode {

    private final ExprNode expr;

    public IterHasNextNode(Position pos, ExprNode expr) {
        super(pos);
        this.expr = expr;
    }

    public ExprNode getExpr() {
        return expr;
    }

    public Value eval() {
        Value v = expr.eval();
        Preconditions.checkArgument(v.getType() == Value.Type.ITERATOR);
        return new BooleanValue(v.asIterator().hasNext());
    }

    @Override
    void accept(ExprNodeVisitor visitor) {
        visitor.visit(this);

    }
}
