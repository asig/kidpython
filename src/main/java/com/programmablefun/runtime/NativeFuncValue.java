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

public class NativeFuncValue extends Value {

    public interface Iface {
        Value run(List<Value> vals);
    }

    private final Iface func;

    public NativeFuncValue(Iface func) {
        super(Type.FUNCTION);
        this.func = func;
    }

    public Iface getFunc() {
        return func;
    }

    @Override
    public String asString() {
        return "native function";
    }

    @Override
    public String asLiteral() {
        return "native function";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NativeFuncValue that = (NativeFuncValue) o;
        return Objects.equals(func, that.func);
    }

    @Override
    public int hashCode() {
        return Objects.hash(func);
    }

    @Override
    public String toString() {
        return "NativeFuncValue{" + getType().toString() + ":" + func + "}";
    }

}
