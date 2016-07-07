package com.asigner.kidpython.compiler.ast;

import com.asigner.kidpython.compiler.Position;
import com.asigner.kidpython.compiler.ast.expr.ExprNode;
import com.asigner.kidpython.compiler.runtime.Environment;

public class EvalStmt extends Stmt {
    private final ExprNode expr;

    public EvalStmt(Position pos, ExprNode expr) {
        super(pos);
        this.expr = expr;
    }

    @Override
    public void accept(NodeVisitor visitor) {
        visitor.visit(this);
    }

    public ExprNode getExpr() {
        return expr;
    }
}
