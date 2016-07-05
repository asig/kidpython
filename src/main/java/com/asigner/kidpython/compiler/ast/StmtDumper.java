package com.asigner.kidpython.compiler.ast;

import com.google.common.collect.Sets;

import java.util.Set;

public class StmtDumper implements StmtVisitor {

    private Set<Stmt> seen;

    public StmtDumper() {
        seen = Sets.newHashSet();
    }

    @Override
    public void visit(ReturnStmt stmt) {
        if (seen.contains(stmt)) return;
        seen.add(stmt);

        System.err.println(String.format("%08x: %-10s %s",  System.identityHashCode(this), "RETURN", stmt.getExpr()));
    }

    @Override
    public void visit(IfStmt stmt) {
        if (seen.contains(stmt)) return;
        seen.add(stmt);

        System.err.println(String.format("%08x: %-10s true=%08x false=%08x",  System.identityHashCode(this), "IF", System.identityHashCode(stmt.getTrueBranch()), System.identityHashCode(stmt.getNext())));
        dump(stmt.getTrueBranch());
    }

    @Override
    public void visit(EmptyStmt stmt) {
        if (seen.contains(stmt)) return;
        seen.add(stmt);

        System.err.println(String.format("%08x: %-10s",  System.identityHashCode(this), "EMPTY"));
    }

    @Override
    public void visit(CallStmt stmt) {
        if (seen.contains(stmt)) return;
        seen.add(stmt);

        System.err.println(String.format("%08x: %-10s %s",  System.identityHashCode(this), "CALL", stmt.getExpr()));
    }

    @Override
    public void visit(AssignmentStmt stmt) {
        if (seen.contains(stmt)) return;
        seen.add(stmt);

        System.err.println(String.format("%08x: %-10s %s = %s",  System.identityHashCode(this), "ASSIGN", stmt.getVar(), stmt.getExpr()));
    }

    public void dump(Stmt stmt) {
        do {
            stmt.accept(this);
            stmt = stmt.getNext();
        } while (stmt != null && !seen.contains(stmt));
    }
}
