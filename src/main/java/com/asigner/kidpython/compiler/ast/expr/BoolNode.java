package com.asigner.kidpython.compiler.ast.expr;

import com.asigner.kidpython.compiler.Position;
import com.asigner.kidpython.compiler.runtime.Value;

public class BoolNode extends BinaryNode {

    public enum Op {
        AND, OR
    }

    private final Op op;

    public BoolNode(Position pos, Op op, ExprNode left, ExprNode right) {
        super(pos, left, right);
        this.op = op;
    }

    @Override
    public Value eval() {
        boolean val;
        switch(op) {
            case AND:
                // "IF !A THEN FALSE ELSE B"
                val = left.eval().asBool();
                if (!val) {
                    return new Value(false);
                } else {
                    return new Value(right.eval().asBool());
                }
            case OR:
                // "IF A THEN TRUE ELSE B"
                val = left.eval().asBool();
                if (val) {
                    return new Value(true);
                } else {
                    return new Value(right.eval().asBool());
                }
        }
        throw new IllegalStateException("Can't happen");
    }
}
