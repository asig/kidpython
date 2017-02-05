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

package com.programmablefun.runtime;

import com.programmablefun.compiler.Position;

public class ExecutionException extends RuntimeException {
    private final Position pos;
    private final String message;

    public ExecutionException(String message) {
        super();
        this.pos = new Position(0,0);
        this.message = message;
    }

    public ExecutionException(Position pos, String message) {
        super();
        this.pos = pos;
        this.message = String.format("On line %d, column %d: %s", pos.getLine(), pos.getCol(), message);
    }

    public Position getPos() {
        return pos;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
