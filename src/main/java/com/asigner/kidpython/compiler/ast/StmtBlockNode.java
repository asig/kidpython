package com.asigner.kidpython.compiler.ast;

import com.asigner.kidpython.compiler.Position;
import com.google.common.collect.Lists;

import java.util.List;

public class StmtBlockNode extends StmtNode {

    private final List<StmtNode> stmts = Lists.newArrayList();

    public StmtBlockNode(Position pos) {
        super(pos);
    }

    public void addStmt(StmtNode node) {
        stmts.add(node);
    }
    @Override
    void execute() {
        for (StmtNode node : stmts) {
            node.execute();
        }
    }
}
