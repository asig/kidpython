package com.asigner.kidpython.compiler.ast;

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

public interface NodeVisitor {
    void visit(AssignmentStmt stmt);
    void visit(CallStmt stmt);
    void visit(EmptyStmt stmt);
    void visit(EvalStmt stmt);
    void visit(ForEachStmt stmt);
    void visit(ForStmt stmt);
    void visit(IfStmt stmt);
    void visit(RepeatStmt stmt);
    void visit(ReturnStmt stmt);
    void visit(WhileStmt stmt);

    void visit(ArithOpNode node);
    void visit(BoolNode node);
    void visit(CallNode node);
    void visit(ConstNode node);
    void visit(IterHasNextNode node);
    void visit(IterNextNode node);
    void visit(MakeFuncNode node);
    void visit(MakeIterNode node);
    void visit(MakeListNode node);
    void visit(MakeMapNode node);
    void visit(MapAccessNode node);
    void visit(NotNode node);
    void visit(RelOpNode node);
    void visit(VarNode node);

}
