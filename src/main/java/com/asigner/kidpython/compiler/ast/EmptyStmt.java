// Copyright 2016 Andreas Signer. All rights reserved.

package com.asigner.kidpython.compiler.ast;

import com.asigner.kidpython.compiler.Position;

public class EmptyStmt extends Stmt {

    public EmptyStmt(Position pos) {
        super(pos);
    }

    @Override
    public Stmt execute() {
        return getNext();
    }

    @Override
    public void accept(StmtVisitor visitor) {
        visitor.visit(this);
    }

}




