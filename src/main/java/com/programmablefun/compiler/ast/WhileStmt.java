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

public class WhileStmt extends Stmt {

    private final ExprNode cond;
    private final Stmt body;

    public WhileStmt(Position pos, ExprNode cond, Stmt body) {
        super(pos);
        this.cond = cond;
        this.body = body;
    }

    public ExprNode getCond() {
        return cond;
    }

    public Stmt getBody() {
        return body;
    }

    @Override
    public void accept(NodeVisitor visitor) {
        visitor.visit(this);
    }

}
