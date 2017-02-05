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

import com.programmablefun.compiler.Position;
import com.programmablefun.compiler.ast.expr.ExprNode;

public class ForStmt extends Stmt {

    private final ExprNode ctrlVar;
    private final ExprNode start;
    private final ExprNode end;
    private final ExprNode step;
    private final Stmt body;

    public ForStmt(Position pos, ExprNode ctrlVar, ExprNode start, ExprNode end, ExprNode step, Stmt body) {
        super(pos);
        this.ctrlVar = ctrlVar;
        this.start = start;
        this.end = end;
        this.step = step;
        this.body = body;
    }

    public ExprNode getCtrlVar() {
        return ctrlVar;
    }

    public ExprNode getStart() {
        return start;
    }

    public ExprNode getEnd() {
        return end;
    }

    public ExprNode getStep() {
        return step;
    }

    public Stmt getBody() {
        return body;
    }

    @Override
    public void accept(NodeVisitor visitor) {
        visitor.visit(this);
    }

}
