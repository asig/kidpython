package com.asigner.kidpython.compiler.ast;

import com.asigner.kidpython.compiler.Position;

public abstract class StmtNode extends Node {

    protected StmtNode(Position pos) {
        super(pos);
    }

    abstract void execute();
}
