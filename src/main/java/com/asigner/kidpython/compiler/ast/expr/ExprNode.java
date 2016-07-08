package com.asigner.kidpython.compiler.ast.expr;

import com.asigner.kidpython.compiler.Position;
import com.asigner.kidpython.compiler.ast.Node;

public abstract class ExprNode extends Node {

    protected ExprNode(Position pos) {
        super(pos);
    }
}
