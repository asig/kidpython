// Copyright 2016 Andreas Signer. All rights reserved.

package com.asigner.kidpython.compiler.ast.expr;

import com.asigner.kidpython.compiler.Position;
import com.asigner.kidpython.compiler.runtime.Environment;
import com.asigner.kidpython.compiler.runtime.Value;

public class VarNode extends ExprNode implements Assignable {

    private final String var;

    public VarNode(Position pos, String var) {
        super(pos);
        this.var = var;
    }

    public String getVar() {
        return var;
    }

    public Value eval(Environment env) {
        return env.getVar(var);
    }

    @Override
    public void assign(Environment env, Value val) {
        env.setVar(var, val);
    }

    @Override
    void accept(ExprNodeVisitor visitor) {
        visitor.visit(this);
    }
}
