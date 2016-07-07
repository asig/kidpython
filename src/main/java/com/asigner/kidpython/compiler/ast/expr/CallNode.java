// Copyright 2016 Andreas Signer. All rights reserved.

package com.asigner.kidpython.compiler.ast.expr;

import com.asigner.kidpython.compiler.Position;
import com.asigner.kidpython.compiler.runtime.Environment;
import com.asigner.kidpython.compiler.runtime.ExecutionException;
import com.asigner.kidpython.compiler.runtime.FuncValue;
import com.asigner.kidpython.compiler.runtime.Value;

import java.util.List;

import static java.util.stream.Collectors.toList;

public class CallNode extends ExprNode {

    private final ExprNode expr;
    private final List<ExprNode> params;

    public CallNode(Position pos, ExprNode expr, List<ExprNode> params) {
        super(pos);
        this.expr = expr;
        this.params = params;
    }

    @Override
    public Value eval(Environment env) {
        Value v = expr.eval(env);
        if (v.getType() != Value.Type.FUNCTION) {
            throw new ExecutionException("Can't call non-function object");
        }

        return null;
    }

    public ExprNode getExpr() {
        return expr;
    }

    public List<ExprNode> getParams() {
        return params;
    }

    @Override
    void accept(ExprNodeVisitor visitor) {
        visitor.visit(this);

    }
}
