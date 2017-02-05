/*
 * Copyright (c) 2017 Andreas Signer <asigner@gmail.com>
 *
 * This file is part of programmablefun.
 *
 * programmablefun is free software: you can redistribute it and/or
 * modify it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * programmablefun is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with programmablefun.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.programmablefun.compiler;

import com.programmablefun.compiler.ast.AssignmentStmt;
import com.programmablefun.compiler.ast.CaseStmt;
import com.programmablefun.compiler.ast.EmptyStmt;
import com.programmablefun.compiler.ast.EvalStmt;
import com.programmablefun.compiler.ast.ForEachStmt;
import com.programmablefun.compiler.ast.ForStmt;
import com.programmablefun.compiler.ast.IfStmt;
import com.programmablefun.compiler.ast.NodeVisitor;
import com.programmablefun.compiler.ast.RepeatStmt;
import com.programmablefun.compiler.ast.ReturnStmt;
import com.programmablefun.compiler.ast.Stmt;
import com.programmablefun.compiler.ast.WhileStmt;
import com.programmablefun.compiler.ast.expr.BinOpNode;
import com.programmablefun.compiler.ast.expr.CallNode;
import com.programmablefun.compiler.ast.expr.ConstNode;
import com.programmablefun.compiler.ast.expr.ExprNode;
import com.programmablefun.compiler.ast.expr.MakeFuncNode;
import com.programmablefun.compiler.ast.expr.MakeIterNode;
import com.programmablefun.compiler.ast.expr.MakeListNode;
import com.programmablefun.compiler.ast.expr.MakeMapNode;
import com.programmablefun.compiler.ast.expr.MapAccessNode;
import com.programmablefun.compiler.ast.expr.RangeNode;
import com.programmablefun.compiler.ast.expr.UnOpNode;
import com.programmablefun.compiler.ast.expr.VarNode;
import com.programmablefun.runtime.BooleanValue;
import com.programmablefun.runtime.FuncValue;
import com.programmablefun.runtime.Instruction;
import com.programmablefun.runtime.NumberValue;
import com.programmablefun.runtime.VarRefValue;
import com.programmablefun.runtime.VarType;
import com.programmablefun.util.Pair;
import com.google.common.collect.Lists;

import java.math.BigDecimal;
import java.util.List;

import static com.programmablefun.runtime.Instruction.OpCode.ADD;
import static com.programmablefun.runtime.Instruction.OpCode.ASSIGN;
import static com.programmablefun.runtime.Instruction.OpCode.B;
import static com.programmablefun.runtime.Instruction.OpCode.BF;
import static com.programmablefun.runtime.Instruction.OpCode.BT;
import static com.programmablefun.runtime.Instruction.OpCode.CALL;
import static com.programmablefun.runtime.Instruction.OpCode.DIV;
import static com.programmablefun.runtime.Instruction.OpCode.DUP;
import static com.programmablefun.runtime.Instruction.OpCode.EQ;
import static com.programmablefun.runtime.Instruction.OpCode.GE;
import static com.programmablefun.runtime.Instruction.OpCode.GT;
import static com.programmablefun.runtime.Instruction.OpCode.IN;
import static com.programmablefun.runtime.Instruction.OpCode.ITER_HAS_NEXT;
import static com.programmablefun.runtime.Instruction.OpCode.ITER_NEXT;
import static com.programmablefun.runtime.Instruction.OpCode.LE;
import static com.programmablefun.runtime.Instruction.OpCode.LT;
import static com.programmablefun.runtime.Instruction.OpCode.MKFIELDREF;
import static com.programmablefun.runtime.Instruction.OpCode.MKITER;
import static com.programmablefun.runtime.Instruction.OpCode.MKLIST;
import static com.programmablefun.runtime.Instruction.OpCode.MKMAP;
import static com.programmablefun.runtime.Instruction.OpCode.MKRANGE;
import static com.programmablefun.runtime.Instruction.OpCode.MUL;
import static com.programmablefun.runtime.Instruction.OpCode.NE;
import static com.programmablefun.runtime.Instruction.OpCode.NEG;
import static com.programmablefun.runtime.Instruction.OpCode.NOT;
import static com.programmablefun.runtime.Instruction.OpCode.POP;
import static com.programmablefun.runtime.Instruction.OpCode.PUSH;
import static com.programmablefun.runtime.Instruction.OpCode.RET;
import static com.programmablefun.runtime.Instruction.OpCode.STOP;
import static com.programmablefun.runtime.Instruction.OpCode.SUB;

public class CodeGenerator implements NodeVisitor {

    private final Stmt stmt;

    private int tmpVarCnt;
    private List<Instruction> instrs;

    public CodeGenerator(Stmt stmt) {
        this.stmt = stmt;
        this.instrs = Lists.newLinkedList();
        this.tmpVarCnt = 0;
    }

    public List<Instruction> generate() {
        generateStmtBlock(stmt);
        emit(new Instruction(tail(stmt), STOP));
        return instrs;
    }

    private Stmt tail(Stmt stmt) {
        Stmt tail = stmt;
        while (tail.getNext() != null) {
            tail = tail.getNext();
        }
        return tail;
    }

    private String makeTempVarName() {
        return String.format("$tmp%04d", tmpVarCnt++);
    }

    private void generateStmtBlock(Stmt stmt) {
        while (stmt != null) {
            stmt.accept(this);
            stmt = stmt.getNext();
        }
    }

    @Override
    public void visit(AssignmentStmt stmt) {
        stmt.getVar().accept(this);
        stmt.getExpr().accept(this);
        emit(new Instruction(stmt, ASSIGN));
    }

    @Override
    public void visit(EmptyStmt stmt) {
    }

    @Override
    public void visit(EvalStmt stmt) {
        stmt.getExpr().accept(this);
        emit(new Instruction(stmt, POP));
    }

    @Override
    public void visit(ForEachStmt stmt) {
        // iter = range.begin();
        VarNode iterVar = new VarNode(stmt.getPos(), makeTempVarName(), VarType.TEMPORARY);
        iterVar.accept(this);
        stmt.getRange().accept(this);
        emit(new Instruction(stmt, MKITER));
        emit(new Instruction(stmt, ASSIGN));

        // if !iter.hasnext goto end
        int loopPc = emit(new Instruction(stmt, PUSH, new VarRefValue(iterVar)));
        emit(new Instruction(stmt, ITER_HAS_NEXT));
        int branchFalsePc = emit(new Instruction(stmt, BF, 0));

        // ctrlVar = iter.next()
        stmt.getCtrlVar().accept(this);
        emit(new Instruction(stmt, PUSH, new VarRefValue(iterVar)));
        emit(new Instruction(stmt, ITER_NEXT));
        emit(new Instruction(stmt, ASSIGN));

        generateStmtBlock(stmt.getBody());

        emit(new Instruction(stmt, B, loopPc));

        patch(branchFalsePc, new Instruction(stmt, BF, instrs.size()));
    }

    @Override
    public void visit(ForStmt stmt) {
        // for i = 0 to 5 step 1

        // i = 0
        stmt.getCtrlVar().accept(this);
        stmt.getStart().accept(this);
        emit(new Instruction(stmt, ASSIGN));

        // if step > 0 then
        //   if i >= end goto end-of-loop
        // else
        //   if i <= end goto end-of-loop
        int loopStartPc = instrs.size();
        stmt.getStep().accept(this);
        emit(new Instruction(stmt, PUSH, new NumberValue(BigDecimal.ZERO)));
        emit(new Instruction(stmt, GT));
        int branchToStepNegativePc = emit(new Instruction(stmt, BF, 0));
        stmt.getCtrlVar().accept(this);
        stmt.getEnd().accept(this);
        emit(new Instruction(stmt, GT));
        int branchToEnd1Pc = emit(new Instruction(stmt, BT, 0));
        int branchToLoopBodyPc = emit(new Instruction(stmt, B, 0));

        int stepNegative = instrs.size();
        patch(branchToStepNegativePc, new Instruction(stmt, BF, stepNegative));

        stmt.getCtrlVar().accept(this);
        stmt.getEnd().accept(this);
        emit(new Instruction(stmt, LT));
        int branchToEnd2Pc = emit(new Instruction(stmt, BT, 0));

        int loopBodyPc = instrs.size();
        patch(branchToLoopBodyPc, new Instruction(stmt, B, loopBodyPc));

        generateStmtBlock(stmt.getBody());

        // i = i + step
        stmt.getCtrlVar().accept(this);
        stmt.getCtrlVar().accept(this);
        stmt.getStep().accept(this);
        emit(new Instruction(stmt, ADD));
        emit(new Instruction(stmt, ASSIGN));

        // jump loopStart
        emit(new Instruction(stmt, B, loopStartPc));
        patch(branchToEnd1Pc, new Instruction(stmt, BT, instrs.size()));
        patch(branchToEnd2Pc, new Instruction(stmt, BT, instrs.size()));
    }

    @Override
    public void visit(IfStmt stmt) {
        stmt.getCond().accept(this);
        int branchIfFalsePc = emit(new Instruction(stmt, BF, 0)); // patch later
        generateStmtBlock(stmt.getTrueBranch());
        int branchPc = emit(new Instruction(stmt, B, 0)); // patch later
        int falseStart = instrs.size();
        generateStmtBlock(stmt.getFalseBranch());
        int pc = instrs.size();
        patch(branchIfFalsePc, new Instruction(stmt, BF, falseStart));
        patch(branchPc, new Instruction(stmt, B, pc));
    }

    @Override
    public void visit(RepeatStmt stmt) {
        int startPc = instrs.size();
        generateStmtBlock(stmt.getBody());
        stmt.getCond().accept(this);
        emit(new Instruction(stmt, BF, startPc));
    }

    @Override
    public void visit(ReturnStmt stmt) {
        stmt.getExpr().accept(this);
        emit(new Instruction(stmt, RET));
    }

    @Override
    public void visit(WhileStmt stmt) {
        int startPc = instrs.size();
        stmt.getCond().accept(this);
        int jumpPc = instrs.size();
        emit(new Instruction(stmt, BF, 0)); // will be patched afterwards
        generateStmtBlock(stmt.getBody());
        emit(new Instruction(stmt, B, startPc)); // will be patched afterwards
        patch(jumpPc, new Instruction(stmt, BF, instrs.size()));
    }

    @Override
    public void visit(CaseStmt stmt) {
        stmt.getCond().accept(this);

        List<Integer> jumpsToEnd = Lists.newArrayList();
        for (CaseStmt.Case c : stmt.getCases()) {
            List<Integer> jumpsToBody = Lists.newArrayList();
            for (ExprNode e : c.getLabelRanges()) {
                emit(new Instruction(e, DUP));
                e.accept(this);
                emit(new Instruction(e, e instanceof RangeNode ? IN : EQ));
                jumpsToBody.add(instrs.size());
                emit(new Instruction(e, BT, 0)); // will be patched later
            }
            int jumpToNextCase = instrs.size();
            emit(new Instruction(stmt, B, 0));

            int bodyStart = instrs.size();
            patch(jumpsToBody, new Instruction(stmt, BT, bodyStart));
            c.getBody().accept(this);
            jumpsToEnd.add(instrs.size());
            emit(new Instruction(stmt, B, 0));

            int nextCasePc = instrs.size();
            patch(jumpToNextCase, new Instruction(stmt, B, nextCasePc));
        }
        int endPc = instrs.size();
        emit(new Instruction(stmt, POP));
        patch(jumpsToEnd, new Instruction(stmt, B, endPc));
    }

    @Override
    public void visit(BinOpNode node) {
        if (node.getOp() == BinOpNode.Op.AND) {
            // IF a THEN b ELSE false
            node.getLeft().accept(this);
            int bf = emit(new Instruction(node, BF, 0));
            node.getRight().accept(this);
            int endOfTrue = emit(new Instruction(node, B, 0));
            int falseStart = emit(new Instruction(node, PUSH, new BooleanValue(false)));
            int end = instrs.size();

            patch(bf, new Instruction(node, BF, falseStart));
            patch(endOfTrue, new Instruction(node, B, end));
        } else if (node.getOp() == BinOpNode.Op.OR) {
            // IF a THEN true ELSE b
            node.getLeft().accept(this);
            int bf = emit(new Instruction(node, BF, 0));
            emit(new Instruction(node, PUSH, new BooleanValue(true)));
            int endOfTrue = emit(new Instruction(node, B, 0));
            int falseStart = instrs.size();
            node.getRight().accept(this);
            int end = instrs.size();

            patch(bf, new Instruction(node, BF, falseStart));
            patch(endOfTrue, new Instruction(node, B, end));
        } else {
            node.getLeft().accept(this);
            node.getRight().accept(this);
            Instruction.OpCode opCode;
            switch (node.getOp()) {
                case EQ:
                    opCode = EQ;
                    break;
                case NE:
                    opCode = NE;
                    break;
                case LE:
                    opCode = LE;
                    break;
                case LT:
                    opCode = LT;
                    break;
                case GE:
                    opCode = GE;
                    break;
                case GT:
                    opCode = GT;
                    break;

                case ADD:
                    opCode = ADD;
                    break;
                case SUB:
                    opCode = SUB;
                    break;
                case MUL:
                    opCode = MUL;
                    break;
                case DIV:
                    opCode = DIV;
                    break;

                default:
                    throw new IllegalStateException("Unknown BinOpNode op " + node.getOp());
            }
            emit(new Instruction(node, opCode));
        }
    }


    @Override
    public void visit(UnOpNode node) {
        node.getExpr().accept(this);
        Instruction.OpCode opCode;
        switch(node.getOp()) {
            case NEG: opCode = NEG; break;
            case NOT: opCode = NOT; break;
            case ITER_NEXT: opCode = ITER_NEXT; break;
            case ITER_HAS_NEXT: opCode = ITER_HAS_NEXT; break;
            default: throw new IllegalStateException("Unknown UnOpNode op " + node.getOp());
        }
        emit(new Instruction(node, opCode));
    }

    @Override
    public void visit(CallNode node) {
        node.getExpr().accept(this);
        List<ExprNode> params = node.getParams();
        for (ExprNode p : params) {
            p.accept(this);
        }
        emit(new Instruction(node, CALL, params.size()));
    }

    @Override
    public void visit(ConstNode node) {
        emit(new Instruction(node, PUSH, node.getVal()));
    }

    @Override
    public void visit(MakeFuncNode node) {
        // jump over function for now.
        int jumpOverFunc = emit(new Instruction(node, B, 0));
        int startPc = instrs.size();
        generateStmtBlock(node.getBody());
        patch(jumpOverFunc, new Instruction(node, B, instrs.size()));
        emit(new Instruction(node, PUSH, new FuncValue(startPc, node.getParams())));
    }

    @Override
    public void visit(MakeIterNode node) {
        node.getNode().accept(this);
        emit(new Instruction(node, MKITER));
    }

    @Override
    public void visit(MakeListNode node) {
        for (ExprNode elem : node.getElements()) {
            elem.accept(this);
        }
        emit(new Instruction(node, MKLIST, node.getElements().size()));
    }

    @Override
    public void visit(MakeMapNode node) {
        for (Pair<ExprNode, ExprNode> elem : node.getElements()) {
            elem.getFirst().accept(this);
            elem.getSecond().accept(this);
        }
        emit(new Instruction(node, MKMAP, node.getElements().size()));
    }

    @Override
    public void visit(MapAccessNode node) {
        node.getMapExpr().accept(this);
        node.getKeyExpr().accept(this);
        emit(new Instruction(node, MKFIELDREF));
    }

    @Override
    public void visit(VarNode node) {
        emit(new Instruction(node, PUSH, new VarRefValue(node)));
    }

    @Override
    public void visit(RangeNode node) {
        node.getStart().accept(this);
        node.getEnd().accept(this);
        emit(new Instruction(node, MKRANGE));
    }

    private int emit(Instruction instr) {
        instrs.add(instr);
        return instrs.size() - 1;
    }

    private void patch(List<Integer> p, Instruction instr) {
        p.forEach(pos -> patch(pos, instr));
    }

    private void patch(int pos, Instruction instr) {
        instrs.set(pos, instr);
    }
}
