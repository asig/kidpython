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

package com.programmablefun.compiler.ast.expr;

import com.programmablefun.compiler.Position;
import com.programmablefun.compiler.ast.NodeVisitor;

public class UnOpNode extends ExprNode {

    public enum Op {
        NEG, NOT, ITER_NEXT, ITER_HAS_NEXT
    };

    private final UnOpNode.Op op;
    private final ExprNode expr;

    public UnOpNode(Position pos, UnOpNode.Op op, ExprNode expr) {
        super(pos);
        this.expr = expr;
        this.op = op;
    }

    public UnOpNode.Op getOp() {
        return op;
    }

    public ExprNode getExpr() {
        return expr;
    }

    @Override
    public void accept(NodeVisitor visitor) {
        visitor.visit(this);
    }
}
