// Copyright 2016 Andreas Signer. All rights reserved.

package com.asigner.kidpython.compiler.ast.expr;

import com.asigner.kidpython.compiler.Position;
import com.asigner.kidpython.compiler.ast.NodeVisitor;

public class MapAccessNode extends ExprNode implements Assignable {

    private final ExprNode mapExpr;
    private final ExprNode keyExpr;

    public MapAccessNode(Position pos, ExprNode mapExpr, ExprNode keyExpr) {
        super(pos);
        this.mapExpr = mapExpr;
        this.keyExpr = keyExpr;
    }

    public ExprNode getKeyExpr() {
        return keyExpr;
    }

    public ExprNode getMapExpr() {
        return mapExpr;
    }

    @Override
    public void accept(NodeVisitor visitor) {
        visitor.visit(this);
    }
}
