// Copyright 2016 Andreas Signer. All rights reserved.

package com.asigner.kidpython.compiler.ast.expr;

import com.asigner.kidpython.compiler.Position;
import com.asigner.kidpython.compiler.runtime.Environment;
import com.asigner.kidpython.compiler.runtime.IterValue;
import com.asigner.kidpython.compiler.runtime.Value;

public class MakeIterNode extends ExprNode {

    private final ExprNode node;

    public MakeIterNode(Position pos, ExprNode node) {
        super(pos);
        this.node = node;
    }

    public ExprNode getNode() {
        return node;
    }

    public Value eval(Environment env) {
        return new IterValue(node.eval(env).asIterator());
    }

    @Override
    void accept(ExprNodeVisitor visitor) {
        visitor.visit(this);

    }
}
