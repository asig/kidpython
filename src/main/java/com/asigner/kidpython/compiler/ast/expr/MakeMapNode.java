package com.asigner.kidpython.compiler.ast.expr;

import com.asigner.kidpython.compiler.Position;
import com.asigner.kidpython.compiler.runtime.Environment;
import com.asigner.kidpython.compiler.runtime.MapValue;
import com.asigner.kidpython.compiler.runtime.Value;
import com.asigner.kidpython.util.Pair;

import java.util.List;

import static java.util.stream.Collectors.toMap;

public class MakeMapNode extends ExprNode {

    private final List<Pair<ExprNode, ExprNode>> nodes;

    public MakeMapNode(Position pos, List<Pair<ExprNode, ExprNode>> nodes) {
        super(pos);
        this.nodes = nodes;
    }

    public List<Pair<ExprNode, ExprNode>> getElements() {
        return nodes;
    }

    public Value eval(Environment env) {
        return new MapValue(
                nodes.stream()
                        .collect(toMap(p -> p.getFirst().eval(env), p -> p.getSecond().eval(env)))
        );
    }

    @Override
    void accept(ExprNodeVisitor visitor) {
        visitor.visit(this);

    }
}
