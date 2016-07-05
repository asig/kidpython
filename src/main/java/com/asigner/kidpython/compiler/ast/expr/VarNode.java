// Copyright 2016 Andreas Signer. All rights reserved.

package com.asigner.kidpython.compiler.ast.expr;

import com.asigner.kidpython.compiler.Position;
import com.asigner.kidpython.compiler.runtime.NumberValue;
import com.asigner.kidpython.compiler.runtime.Value;

import java.math.BigDecimal;

public class VarNode extends ExprNode {

    private final String var;

    public VarNode(Position pos, String var) {
        super(pos);
        this.var = var;
    }

    public String getVar() {
        return var;
    }

    public Value eval() {
        // TODO(asigner): get value
        return new NumberValue(BigDecimal.ZERO);
    }

    @Override
    void accept(ExprNodeVisitor visitor) {
        visitor.visit(this);

    }
}
