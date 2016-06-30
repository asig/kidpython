package com.asigner.kidpython.compiler.ast;

import com.asigner.kidpython.compiler.Position;

public class Node {

    private final Position pos;

    protected Node(Position pos) {
        this.pos = pos;
    }

    public Position getPos() {
        return pos;
    }
}
