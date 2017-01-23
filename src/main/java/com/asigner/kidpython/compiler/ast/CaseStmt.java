// Copyright 2017 Andreas Signer. All rights reserved.

package com.asigner.kidpython.compiler.ast;

import com.asigner.kidpython.compiler.Position;
import com.asigner.kidpython.compiler.ast.expr.ExprNode;

import java.util.List;

public class CaseStmt extends Stmt {

    public static class Case {
        private final List<ExprNode> labelRanges;
        private Stmt body;

        public Case(List<ExprNode> labelRanges, Stmt body) {
            this.labelRanges = labelRanges;
            this.body = body;
        }

        public List<ExprNode> getLabelRanges() {
            return labelRanges;
        }

        public Stmt getBody() {
            return body;
        }
    }

    private ExprNode cond;
    private List<Case> parts;

    public CaseStmt(Position pos, ExprNode cond, List<Case> parts) {
        super(pos);
        this.cond = cond;
        this.parts = parts;
    }

    public ExprNode getCond() {
        return cond;
    }

    public List<Case> getCases() {
        return parts;
    }

    @Override
    public void accept(NodeVisitor visitor) {
        visitor.visit(this);
    }

}
