// Copyright 2016 Andreas Signer. All rights reserved.

package com.asigner.kidpython.compiler.ast.expr;

import com.asigner.kidpython.compiler.Position;
import com.asigner.kidpython.compiler.runtime.BooleanValue;
import com.asigner.kidpython.compiler.runtime.IterValue;
import com.asigner.kidpython.compiler.runtime.Value;
import com.google.common.base.Preconditions;

public class IterNextNode extends ExprNode {

    private final ExprNode expr;

    public IterNextNode(Position pos, ExprNode expr) {
        super(pos);
        this.expr = expr;
    }

    public Value eval() {
        Value v = expr.eval();
        Preconditions.checkArgument(v.getType() == Value.Type.ITERATOR);
        return v.asIterator().next();
    }
}
