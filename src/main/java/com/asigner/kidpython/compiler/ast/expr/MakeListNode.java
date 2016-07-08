package com.asigner.kidpython.compiler.ast.expr;

import com.asigner.kidpython.compiler.Position;
import com.asigner.kidpython.compiler.ast.NodeVisitor;

import java.util.List;

public class MakeListNode extends ExprNode {

    private final List<ExprNode> nodes;

    public MakeListNode(Position pos, List<ExprNode> nodes) {
        super(pos);
        this.nodes = nodes;
    }

    public List<ExprNode> getElements() {
        return nodes;
    }

    @Override
    public void accept(NodeVisitor visitor) {
        visitor.visit(this);
    }
}
