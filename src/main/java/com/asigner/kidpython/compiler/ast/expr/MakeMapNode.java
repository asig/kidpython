package com.asigner.kidpython.compiler.ast.expr;

import com.asigner.kidpython.compiler.Position;
import com.asigner.kidpython.compiler.ast.NodeVisitor;
import com.asigner.kidpython.util.Pair;

import java.util.List;

public class MakeMapNode extends ExprNode {

    private final List<Pair<ExprNode, ExprNode>> nodes;

    public MakeMapNode(Position pos, List<Pair<ExprNode, ExprNode>> nodes) {
        super(pos);
        this.nodes = nodes;
    }

    public List<Pair<ExprNode, ExprNode>> getElements() {
        return nodes;
    }

    @Override
    public void accept(NodeVisitor visitor) {
        visitor.visit(this);
    }
}
