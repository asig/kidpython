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

public class MapAccessNode extends ExprNode {

    private final ExprNode mapExpr;
    private final ExprNode keyExpr;

    public MapAccessNode(Position pos, ExprNode mapExpr, ExprNode keyExpr) {
        super(pos);
        this.mapExpr = mapExpr;
        this.keyExpr = keyExpr;
    }

    public ExprNode getKeyExpr() {
        return keyExpr;
    }

    public ExprNode getMapExpr() {
        return mapExpr;
    }

    @Override
    public void accept(NodeVisitor visitor) {
        visitor.visit(this);
    }
}
