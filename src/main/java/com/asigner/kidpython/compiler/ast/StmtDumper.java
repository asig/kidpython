package com.asigner.kidpython.compiler.ast;

import com.asigner.kidpython.compiler.ast.expr.ExprDumper;
import com.asigner.kidpython.compiler.ast.expr.ExprNode;
import com.google.common.collect.Sets;

import java.io.PrintStream;
import java.util.Set;

public class StmtDumper implements StmtVisitor {

    private Set<Stmt> seen;
    private PrintStream printStream;

    public StmtDumper(PrintStream printStream)
    {
        this.printStream = printStream;
        seen = Sets.newHashSet();
    }

    @Override
    public void visit(ReturnStmt stmt) {
        if (seen.contains(stmt)) return;
        seen.add(stmt);

        printStream.println(String.format("%08x: %-10s %s",  System.identityHashCode(stmt), "RETURN", dumpExpr(stmt.getExpr())));
    }

    @Override
    public void visit(IfStmt stmt) {
        if (seen.contains(stmt)) return;
        seen.add(stmt);

        printStream.println(String.format("%08x: %-10s true=%08x false=%08x",  System.identityHashCode(stmt), "IF", System.identityHashCode(stmt.getTrueBranch()), System.identityHashCode(stmt.getNext())));
        dump(stmt.getTrueBranch());
    }

    @Override
    public void visit(EmptyStmt stmt) {
        if (seen.contains(stmt)) return;
        seen.add(stmt);

        printStream.println(String.format("%08x: %-10s",  System.identityHashCode(stmt), "EMPTY"));
    }

    @Override
    public void visit(CallStmt stmt) {
        if (seen.contains(stmt)) return;
        seen.add(stmt);

        printStream.println(String.format("%08x: %-10s %s",  System.identityHashCode(stmt), "CALL", dumpExpr(stmt.getExpr())));
    }

    @Override
    public void visit(AssignmentStmt stmt) {
        if (seen.contains(stmt)) return;
        seen.add(stmt);

        printStream.println(String.format("%08x: %-10s %s = %s",  System.identityHashCode(stmt), "ASSIGN", dumpExpr(stmt.getVar()), dumpExpr(stmt.getExpr())));
    }

    public void dump(Stmt stmt) {
        do {
            stmt.accept(this);
            stmt = stmt.getNext();
        } while (stmt != null && !seen.contains(stmt));
    }

    private String dumpExpr(ExprNode n) {
        return new ExprDumper().dump(n);
    }
}
