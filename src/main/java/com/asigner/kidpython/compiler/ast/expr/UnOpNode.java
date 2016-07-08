package com.asigner.kidpython.compiler.ast.expr;

import com.asigner.kidpython.compiler.Position;
import com.asigner.kidpython.compiler.ast.NodeVisitor;

public class UnOpNode extends ExprNode {

    public enum Op {
        NEG, NOT, ITER_NEXT, ITER_HAS_NEXT
    };

    private final UnOpNode.Op op;
    private final ExprNode expr;

    public UnOpNode(Position pos, UnOpNode.Op op, ExprNode expr) {
        super(pos);
        this.expr = expr;
        this.op = op;
    }

    public UnOpNode.Op getOp() {
        return op;
    }

    public ExprNode getExpr() {
        return expr;
    }

    @Override
    public void accept(NodeVisitor visitor) {
        visitor.visit(this);
    }
}
