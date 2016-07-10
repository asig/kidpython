package com.asigner.kidpython.compiler.ast;

import com.asigner.kidpython.compiler.Position;

public abstract class Stmt extends Node {

    protected Stmt next;

    protected Stmt(Position pos) {
        super(pos);
    }

    public void setNext(Stmt next) {
        this.next = next;
    }

    public Stmt getNext() {
        return next;
    }

    public abstract void accept(NodeVisitor visitor);
}
