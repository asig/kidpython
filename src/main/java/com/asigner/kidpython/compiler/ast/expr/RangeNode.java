package com.asigner.kidpython.compiler.ast.expr;

import com.asigner.kidpython.compiler.Position;
import com.asigner.kidpython.compiler.ast.NodeVisitor;

public class RangeNode extends ExprNode {
    private final ExprNode start, end;

    public RangeNode(Position pos, ExprNode start, ExprNode end) {
        super(pos);
        this.start = start;
        this.end = end;
    }

    public ExprNode getStart() {
        return start;
    }

    public ExprNode getEnd() {
        return end;
    }

    @Override
    public void accept(NodeVisitor visitor) {
        visitor.visit(this);
    }
}
