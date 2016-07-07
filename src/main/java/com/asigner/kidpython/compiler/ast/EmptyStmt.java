// Copyright 2016 Andreas Signer. All rights reserved.

package com.asigner.kidpython.compiler.ast;

import com.asigner.kidpython.compiler.Position;
import com.asigner.kidpython.compiler.runtime.Environment;

public class EmptyStmt extends Stmt {

    public EmptyStmt(Position pos) {
        super(pos);
    }

    @Override
    public void accept(NodeVisitor visitor) {
        visitor.visit(this);
    }

}




