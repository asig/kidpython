// Copyright 2016 Andreas Signer. All rights reserved.

package com.asigner.kidpython.compiler.ast.expr;

import com.asigner.kidpython.util.Pair;
import com.google.common.collect.Lists;

import java.util.List;

import static java.util.stream.Collectors.joining;

public class ExprDumper implements ExprNodeVisitor {

    private String dump;

    @Override
    public void visit(ArithOpNode node) {
        String op = "?";
        switch (node.getOp()) {
            case ADD: op = "+"; break;
            case SUB: op = "-"; break;
            case MUL: op = "*"; break;
            case DIV: op = "/"; break;
        }
        dump = "";
        node.getLeft().accept(this);
        String leftStr = dump;
        dump = "";
        node.getRight().accept(this);

        dump = "(" + leftStr + ") " + op + " (" + dump + ")";
    }

    @Override
    public void visit(VarNode node) {
        dump = node.getVar();

    }

    @Override
    public void visit(RelOpNode node) {
        String op = "?";
        switch (node.getOp()) {
            case EQ: op = "="; break;
            case NE: op = "<>"; break;
            case LE: op = "<="; break;
            case LT: op = "<"; break;
            case GE: op = ">="; break;
            case GT: op = ">"; break;
        }
        dump = "";
        node.getLeft().accept(this);
        String leftStr = dump;
        dump = "";
        node.getRight().accept(this);

        dump = "(" + leftStr + ") " + op + " (" + dump + ")";
    }

    @Override
    public void visit(MapAccessNode node) {
        node.getMapExpr().accept(this);
        String map = dump;
        dump = "";
        node.getKeyExpr().accept(this);
        dump = "(" + map + ")[" + dump + "]";
    }

    @Override
    public void visit(MakeMapNode node) {
        List<String> initializers = Lists.newArrayList();
        for(Pair<ExprNode, ExprNode> p : node.getElements()) {
            dump = "";
            p.getFirst().accept(this);
            String key = dump;
            dump = "";
            p.getSecond().accept(this);
            initializers.add(key + ":" + dump);
        }
        dump = "{" + initializers.stream().collect(joining(",")) + "}";
    }

    @Override
    public void visit(MakeListNode node) {
        List<String> initializers = Lists.newArrayList();
        for(ExprNode e : node.getElements()) {
            dump = "";
            e.accept(this);
            initializers.add(dump);
        }
        dump = "[" + initializers.stream().collect(joining(",")) + "]";
    }

    @Override
    public void visit(MakeIterNode node) {
        node.getNode().accept(this);
        dump = "makeIter(" + dump + ")";
    }

    @Override
    public void visit(IterNextNode node) {
        node.getExpr().accept(this);
        dump = "iterNext(" + dump + ")";
    }

    @Override
    public void visit(IterHasNextNode node) {
        node.getExpr().accept(this);
        dump = "hasNext(" + dump + ")";
    }

    @Override
    public void visit(ConstNode node) {
        dump = node.getVal().toString();
    }

    @Override
    public void visit(CallNode node) {
        node.getExpr().accept(this);
        String call = dump;

        List<String> params = Lists.newArrayList();
        for(ExprNode e : node.getParams()) {
            dump = "";
            e.accept(this);
            params.add(dump);
        }
        dump = "call(" + call + ")" + "(" + params.stream().collect(joining(",")) + ")";
    }

    @Override
    public void visit(BoolNode node) {
        String op = "?";
        switch (node.getOp()) {
            case AND: op = "and"; break;
            case OR: op = "or"; break;
        }
        dump = "";
        node.getLeft().accept(this);
        String leftStr = dump;
        dump = "";
        node.getRight().accept(this);

        dump = "(" + leftStr + ") " + op + " (" + dump + ")";
    }

    @Override
    public void visit(MakeFuncNode node) {
        dump = "mkfunc()";
    }

    @Override
    public void visit(NotNode node) {
        node.getExpr().accept(this);
        dump = "not(" + dump + ")";
    }

    public String dump(ExprNode node) {
        dump = "";
        node.accept(this);
        return dump;
    }
}
