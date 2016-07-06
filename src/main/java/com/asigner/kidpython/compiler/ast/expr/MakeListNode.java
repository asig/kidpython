package com.asigner.kidpython.compiler.ast.expr;

import com.asigner.kidpython.compiler.Position;
import com.asigner.kidpython.compiler.runtime.Environment;
import com.asigner.kidpython.compiler.runtime.ListValue;
import com.asigner.kidpython.compiler.runtime.Value;

import java.util.List;
import java.util.stream.Collectors;

public class MakeListNode extends ExprNode {

    private final List<ExprNode> nodes;

    public MakeListNode(Position pos, List<ExprNode> nodes) {
        super(pos);
        this.nodes = nodes;
    }

    public List<ExprNode> getElements() {
        return nodes;
    }

    public Value eval(Environment env) {
        return new ListValue(
                nodes.stream()
                        .map(exprNode -> exprNode.eval(env))
                        .collect(Collectors.toList())
        );
    }

    @Override
    void accept(ExprNodeVisitor visitor) {
        visitor.visit(this);

    }
}
