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

package com.programmablefun.compiler.ast;

import com.programmablefun.compiler.ast.expr.BinOpNode;
import com.programmablefun.compiler.ast.expr.CallNode;
import com.programmablefun.compiler.ast.expr.ConstNode;
import com.programmablefun.compiler.ast.expr.MakeFuncNode;
import com.programmablefun.compiler.ast.expr.MakeIterNode;
import com.programmablefun.compiler.ast.expr.MakeListNode;
import com.programmablefun.compiler.ast.expr.MakeMapNode;
import com.programmablefun.compiler.ast.expr.MapAccessNode;
import com.programmablefun.compiler.ast.expr.RangeNode;
import com.programmablefun.compiler.ast.expr.UnOpNode;
import com.programmablefun.compiler.ast.expr.VarNode;

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
    void visit(CaseStmt stmt);

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
