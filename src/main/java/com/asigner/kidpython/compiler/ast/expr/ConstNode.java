package com.asigner.kidpython.compiler.ast.expr;

import com.asigner.kidpython.compiler.Position;
import com.asigner.kidpython.compiler.runtime.Value;

public class ConstNode extends ExprNode {

    private final Value val;

    public ConstNode(Position pos, Value val) {
        super(pos);
        this.val = val;
    }

    public Value eval() {
        return val;
    }
}