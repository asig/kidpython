package com.asigner.kidpython.compiler.ast.expr;

import com.asigner.kidpython.compiler.Position;
import com.asigner.kidpython.compiler.runtime.BooleanValue;
import com.asigner.kidpython.compiler.runtime.Environment;
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
    public Value eval(Environment env) {
        boolean val;
        switch(op) {
            case AND:
                // "IF !A THEN FALSE ELSE B"
                val = left.eval(env).asBool();
                if (!val) {
                    return new BooleanValue(false);
                } else {
                    return new BooleanValue(right.eval(env).asBool());
                }
            case OR:
                // "IF A THEN TRUE ELSE B"
                val = left.eval(env).asBool();
                if (val) {
                    return new BooleanValue(true);
                } else {
                    return new BooleanValue(right.eval(env).asBool());
                }
        }
        throw new IllegalStateException("Can't happen");
    }

    public Op getOp() {
        return op;
    }

    @Override
    void accept(ExprNodeVisitor visitor) {
        visitor.visit(this);

    }
}
