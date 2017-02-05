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
import com.programmablefun.util.Pair;

import java.util.List;

public class MakeMapNode extends ExprNode {

    private final List<Pair<ExprNode, ExprNode>> nodes;

    public MakeMapNode(Position pos, List<Pair<ExprNode, ExprNode>> nodes) {
        super(pos);
        this.nodes = nodes;
    }

    public List<Pair<ExprNode, ExprNode>> getElements() {
        return nodes;
    }

    @Override
    public void accept(NodeVisitor visitor) {
        visitor.visit(this);
    }
}
