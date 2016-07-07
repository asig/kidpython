// Copyright 2016 Andreas Signer. All rights reserved.

package com.asigner.kidpython.compiler.ast.expr;

import com.asigner.kidpython.compiler.Position;
import com.asigner.kidpython.compiler.runtime.Environment;
import com.asigner.kidpython.compiler.runtime.ExecutionException;
import com.asigner.kidpython.compiler.runtime.Value;

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
    public Value eval(Environment env) {
        // TODO(asigner): Implement me!
        return null;
    }

    @Override
    public void assign(Environment env, Value val) {
        Value raw = mapExpr.eval(env);
        if (raw.getType() != Value.Type.MAP) {
            throw new ExecutionException("Base variable is not a map");
        }
        raw.asMap().put(keyExpr.eval(env), val);
    }

    @Override
    void accept(ExprNodeVisitor visitor) {
        visitor.visit(this);

    }
}
