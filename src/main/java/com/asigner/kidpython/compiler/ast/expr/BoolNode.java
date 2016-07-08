package com.asigner.kidpython.compiler.ast.expr;

import com.asigner.kidpython.compiler.Position;
import com.asigner.kidpython.compiler.ast.NodeVisitor;

public class BoolNode extends BinaryNode {

    public enum Op {
        AND, OR
    }

    private final Op op;

    public BoolNode(Position pos, Op op, ExprNode left, ExprNode right) {
        super(pos, left, right);
        this.op = op;
    }

    public Op getOp() {
        return op;
    }

    @Override
    public void accept(NodeVisitor visitor) {
        visitor.visit(this);
    }
}
