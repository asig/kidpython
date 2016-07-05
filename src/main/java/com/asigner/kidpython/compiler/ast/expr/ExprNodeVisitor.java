// Copyright 2016 Andreas Signer. All rights reserved.

package com.asigner.kidpython.compiler.ast.expr;

public interface ExprNodeVisitor {
    void visit(VarNode node);
    void visit(RelOpNode node);
    void visit(MapAccessNode node);
    void visit(MakeMapNode node);
    void visit(MakeListNode node);
    void visit(MakeIterNode node);
    void visit(IterNextNode node);
    void visit(IterHasNextNode node);
    void visit(ConstNode node);
    void visit(CallNode node);
    void visit(BoolNode node);
    void visit(ArithOpNode node);
    void visit(MakeFuncNode node);
    void visit(NotNode node);
}
