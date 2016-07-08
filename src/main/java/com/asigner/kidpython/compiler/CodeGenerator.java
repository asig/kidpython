// Copyright 2016 Andreas Signer. All rights reserved.

package com.asigner.kidpython.compiler;

import com.asigner.kidpython.compiler.ast.AssignmentStmt;
import com.asigner.kidpython.compiler.ast.CallStmt;
import com.asigner.kidpython.compiler.ast.EmptyStmt;
import com.asigner.kidpython.compiler.ast.EvalStmt;
import com.asigner.kidpython.compiler.ast.ForEachStmt;
import com.asigner.kidpython.compiler.ast.ForStmt;
import com.asigner.kidpython.compiler.ast.IfStmt;
import com.asigner.kidpython.compiler.ast.NodeVisitor;
import com.asigner.kidpython.compiler.ast.RepeatStmt;
import com.asigner.kidpython.compiler.ast.ReturnStmt;
import com.asigner.kidpython.compiler.ast.Stmt;
import com.asigner.kidpython.compiler.ast.WhileStmt;
import com.asigner.kidpython.compiler.ast.expr.ArithOpNode;
import com.asigner.kidpython.compiler.ast.expr.BoolNode;
import com.asigner.kidpython.compiler.ast.expr.CallNode;
import com.asigner.kidpython.compiler.ast.expr.ConstNode;
import com.asigner.kidpython.compiler.ast.expr.IterHasNextNode;
import com.asigner.kidpython.compiler.ast.expr.IterNextNode;
import com.asigner.kidpython.compiler.ast.expr.MakeFuncNode;
import com.asigner.kidpython.compiler.ast.expr.MakeIterNode;
import com.asigner.kidpython.compiler.ast.expr.MakeListNode;
import com.asigner.kidpython.compiler.ast.expr.MakeMapNode;
import com.asigner.kidpython.compiler.ast.expr.MapAccessNode;
import com.asigner.kidpython.compiler.ast.expr.NotNode;
import com.asigner.kidpython.compiler.ast.expr.RelOpNode;
import com.asigner.kidpython.compiler.ast.expr.VarNode;
import com.asigner.kidpython.compiler.runtime.Instruction;
import com.asigner.kidpython.compiler.runtime.ReferenceValue;
import com.google.common.collect.Lists;

import java.util.List;

import static com.asigner.kidpython.compiler.runtime.Instruction.OpCode.EQ;
import static com.asigner.kidpython.compiler.runtime.Instruction.OpCode.GE;
import static com.asigner.kidpython.compiler.runtime.Instruction.OpCode.GT;
import static com.asigner.kidpython.compiler.runtime.Instruction.OpCode.LE;
import static com.asigner.kidpython.compiler.runtime.Instruction.OpCode.LT;
import static com.asigner.kidpython.compiler.runtime.Instruction.OpCode.NE;
import static com.asigner.kidpython.compiler.runtime.Instruction.OpCode.NOT;
import static com.asigner.kidpython.compiler.runtime.Instruction.OpCode.PUSH;

public class CodeGenerator implements NodeVisitor {

    private final Stmt stmt;

    private List<Instruction> instrs;

    public CodeGenerator(Stmt stmt) {
        this.stmt = stmt;
        this.instrs = Lists.newLinkedList();
    }

    public List<Instruction> generate() {
        generateStmtBlock(stmt);
        return instrs;
    }

    private void generateStmtBlock(Stmt stmt) {
        while (stmt != null) {
            stmt.accept(this);
            stmt = stmt.getNext();
        }
    }

    @Override
    public void visit(AssignmentStmt stmt) {

    }

    @Override
    public void visit(CallStmt stmt) {

    }

    @Override
    public void visit(EmptyStmt stmt) {

    }

    @Override
    public void visit(EvalStmt stmt) {

    }

    @Override
    public void visit(ForEachStmt stmt) {

    }

    @Override
    public void visit(ForStmt stmt) {

    }

    @Override
    public void visit(IfStmt stmt) {

    }

    @Override
    public void visit(RepeatStmt stmt) {

    }

    @Override
    public void visit(ReturnStmt stmt) {

    }

    @Override
    public void visit(WhileStmt stmt) {

    }

    @Override
    public void visit(ArithOpNode node) {

    }

    @Override
    public void visit(BoolNode node) {

    }

    @Override
    public void visit(CallNode node) {

    }

    @Override
    public void visit(ConstNode node) {

    }

    @Override
    public void visit(IterHasNextNode node) {

    }

    @Override
    public void visit(IterNextNode node) {

    }

    @Override
    public void visit(MakeFuncNode node) {

    }

    @Override
    public void visit(MakeIterNode node) {

    }

    @Override
    public void visit(MakeListNode node) {

    }

    @Override
    public void visit(MakeMapNode node) {

    }

    @Override
    public void visit(MapAccessNode node) {

    }

    @Override
    public void visit(NotNode node) {
        node.getExpr().accept(this);
        emit(new Instruction(node, NOT));
    }

    @Override
    public void visit(RelOpNode node) {
        node.getLeft().accept(this);
        node.getRight().accept(this);
        Instruction.OpCode opCode;
        switch(node.getOp()) {
            case EQ: opCode = EQ; break;
            case NE: opCode = NE; break;
            case LE: opCode = LE; break;
            case LT: opCode = LT; break;
            case GE: opCode = GE; break;
            case GT: opCode = GT; break;
            default: throw new IllegalStateException("Unknown RelOpNode op " + node.getOp());
        }
        emit(new Instruction(node, opCode));
    }

    @Override
    public void visit(VarNode node) {
        emit(new Instruction(node, PUSH, new ReferenceValue(node)));
    }

    private void emit(Instruction instr) {
        instrs.add(instr);
    }
}
