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

import java.util.List;
import java.util.Objects;

public class FuncValue extends Value {
    private final int startPc;
    private final List<String> params;

    public FuncValue(int startPc, List<String> params) {
        super(Type.FUNCTION);
        this.startPc = startPc;
        this.params = params;
    }

    public int getStartPc() {
        return startPc;
    }

    public List<String> getParams() {
        return params;
    }

    @Override
    public String asString() {
        return "function";
    }

    @Override
    public String asLiteral() {
        return "function";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FuncValue funcValue = (FuncValue) o;

        if (startPc != funcValue.startPc) return false;
        return params != null ? params.equals(funcValue.params) : funcValue.params == null;

    }

    @Override
    public int hashCode() {
        return Objects.hash(startPc, params);
    }

    @Override
    public String toString() {
        return "FuncValue{" + getType().toString() + ":" + startPc + "}";
    }

}
