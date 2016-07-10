// Copyright 2016 Andreas Signer. All rights reserved.

package com.asigner.kidpython.compiler;

import com.asigner.kidpython.compiler.ast.EmptyStmt;
import com.asigner.kidpython.compiler.ast.Stmt;

public class StmtList {

    private final Stmt head = new EmptyStmt(new Position(0,0));
    private Stmt cur = head;

    public void add(Stmt node) {
        cur.setNext(node);
        while (cur.getNext() != null) {
            cur = cur.getNext();
        }
    }

    public Stmt getFirst() {
        return head.getNext();
    }

    public Stmt getLast() {
        return cur;
    }
}
