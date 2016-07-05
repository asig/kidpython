package com.asigner.kidpython.compiler.ast;

public interface StmtVisitor {
    void visit(ReturnStmt stmt);
    void visit(IfStmt stmt);
    void visit(EmptyStmt stmt);
    void visit(CallStmt stmt);
    void visit(AssignmentStmt stmt);
}
