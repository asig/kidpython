// Copyright 2016 Andreas Signer. All rights reserved.

package com.asigner.kidpython.compiler.ast.expr;

import com.asigner.kidpython.compiler.Position;
import com.asigner.kidpython.compiler.runtime.Value;

public class IndexNode extends BinaryNode {

    public IndexNode(Position pos, ExprNode left, ExprNode right) {
        super(pos, left, right);
    }

    @Override
    public Value eval() {
        // TODO(asigner): Implement me!
        return null;
    }
}
