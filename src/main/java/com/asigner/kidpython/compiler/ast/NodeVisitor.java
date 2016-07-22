package com.asigner.kidpython.compiler.ast;

import com.asigner.kidpython.compiler.ast.expr.BinOpNode;
import com.asigner.kidpython.compiler.ast.expr.CallNode;
import com.asigner.kidpython.compiler.ast.expr.ConstNode;
import com.asigner.kidpython.compiler.ast.expr.MakeFuncNode;
import com.asigner.kidpython.compiler.ast.expr.MakeIterNode;
import com.asigner.kidpython.compiler.ast.expr.MakeListNode;
import com.asigner.kidpython.compiler.ast.expr.MakeMapNode;
import com.asigner.kidpython.compiler.ast.expr.MapAccessNode;
import com.asigner.kidpython.compiler.ast.expr.RangeNode;
import com.asigner.kidpython.compiler.ast.expr.UnOpNode;
import com.asigner.kidpython.compiler.ast.expr.VarNode;

public interface NodeVisitor {
    void visit(AssignmentStmt stmt);
    void visit(EmptyStmt stmt);
    void visit(EvalStmt stmt);
    void visit(ForEachStmt stmt);
    void visit(ForStmt stmt);
    void visit(IfStmt stmt);
    void visit(RepeatStmt stmt);
    void visit(ReturnStmt stmt);
    void visit(WhileStmt stmt);

    void visit(BinOpNode node);
    void visit(UnOpNode node);
    void visit(CallNode node);
    void visit(ConstNode node);
    void visit(MakeFuncNode node);
    void visit(MakeIterNode node);
    void visit(MakeListNode node);
    void visit(MakeMapNode node);
    void visit(MapAccessNode node);
    void visit(VarNode node);
    void visit(RangeNode node);

}
