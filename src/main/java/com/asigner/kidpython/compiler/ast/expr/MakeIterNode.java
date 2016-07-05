// Copyright 2016 Andreas Signer. All rights reserved.

package com.asigner.kidpython.compiler.ast.expr;

import com.asigner.kidpython.compiler.Position;
import com.asigner.kidpython.compiler.runtime.IterValue;
import com.asigner.kidpython.compiler.runtime.ListValue;
import com.asigner.kidpython.compiler.runtime.Value;

import java.util.List;
import java.util.stream.Collectors;

public class MakeIterNode extends ExprNode {

    private final ExprNode node;

    public MakeIterNode(Position pos, ExprNode node) {
        super(pos);
        this.node = node;
    }

    public ExprNode getNode() {
        return node;
    }

    public Value eval() {
        return new IterValue(node.eval().asIterator());
    }

    @Override
    void accept(ExprNodeVisitor visitor) {
        visitor.visit(this);

    }
}
