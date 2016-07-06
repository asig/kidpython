package com.asigner.kidpython.compiler.ast;

import com.asigner.kidpython.compiler.Position;
import com.asigner.kidpython.compiler.runtime.Environment;

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

    public Stmt last() {
        Stmt cur = this;
        while (cur.getNext() != null) {
            cur = cur.getNext();
        }
        return cur;
    }

    public abstract Stmt execute(Environment env);

    public abstract void accept(StmtVisitor visitor);
}
