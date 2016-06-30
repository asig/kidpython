package com.asigner.kidpython.compiler.ast;

import com.asigner.kidpython.compiler.Position;
import com.asigner.kidpython.compiler.runtime.Value;

public abstract class ExprNode extends Node {

    protected ExprNode(Position pos) {
        super(pos);
    }

    abstract public Value eval();
}
