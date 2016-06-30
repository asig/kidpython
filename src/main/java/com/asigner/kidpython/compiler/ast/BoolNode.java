package com.asigner.kidpython.compiler.ast;

import com.asigner.kidpython.compiler.Position;
import com.asigner.kidpython.compiler.runtime.Value;

public class BoolNode extends ExprNode {

    enum Op {
        AND, OR
    }

    private final Op op;
    private final ExprNode left;
    private final ExprNode right;

    public BoolNode(Position pos, Op op, ExprNode left, ExprNode right) {
        super(pos);
        this.op = op;
        this.left = left;
        this.right = right;
    }

    @Override
    public Value eval() {
        switch(op) {
            case AND:
                // "IF !A THEN FALSE ELSE B"
                Value val = left.eval();
                
                break;
            case OR:
                // "IF A THEN TRUE ELSE B"
                break;
        }

        //
        return null;
    }
}
