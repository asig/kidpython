package com.asigner.kidpython.compiler.ast;

import com.asigner.kidpython.compiler.Position;

public abstract class Node {

    private final Position pos;

    protected Node(Position pos) {
        this.pos = pos;
    }

    public Position getPos() {
        return pos;
    }

    abstract public void accept(NodeVisitor visitor);
}
