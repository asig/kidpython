// Copyright 2016 Andreas Signer. All rights reserved.

package com.asigner.kidpython.compiler.ast.expr;

import com.asigner.kidpython.compiler.Position;
import com.asigner.kidpython.compiler.runtime.Value;

public class PropertyNode extends ExprNode {

    private final ExprNode expr;
    private final String property;

    public PropertyNode(Position pos, ExprNode expr, String property) {
        super(pos);
        this.expr = expr;
        this.property = property;
    }

    @Override
    public Value eval() {
        // TODO(asigner): Implement me!
        return null;
    }
}
