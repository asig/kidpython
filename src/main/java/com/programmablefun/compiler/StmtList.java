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

import com.programmablefun.compiler.ast.EmptyStmt;
import com.programmablefun.compiler.ast.Stmt;

public class StmtList {

    private final Stmt head = new EmptyStmt(new Position(0,0));
    private Stmt cur = head;

    public void add(Stmt node) {
        cur.setNext(node);
        while (cur.getNext() != null) {
            cur = cur.getNext();
        }
    }

    public Stmt getFirst() {
        return head.getNext();
    }

    public Stmt getLast() {
        return cur;
    }
}
