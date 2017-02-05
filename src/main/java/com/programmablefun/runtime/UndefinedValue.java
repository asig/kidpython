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

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class UndefinedValue extends Value {
    public static final UndefinedValue INSTANCE = new UndefinedValue();

    private UndefinedValue() {
        super(Type.UNDEFINED);
    }

    @Override
    public boolean asBool() {
        throw new RuntimeException("Can't coerce undefined to a boolean");
    }

    @Override
    public String asString() {
        throw new ExecutionException("Can't coerce undefined to a string");
    }

    @Override
    public String asLiteral() {
        return "<null>";
    }

    @Override
    public BigDecimal asNumber() {
        throw new RuntimeException("Can't coerce undefined to a number");
    }

    @Override
    public Iterator<? extends Value> asIterator() {
        throw new ExecutionException("Can't coerce undefined to an iterator");
    };

    @Override
    public List<Value> asList() {
        throw new RuntimeException("Can't coerce undefined to a list");
    }

    @Override
    public Map<Value, Value> asMap() {
        throw new RuntimeException("Can't coerce undefined to a map");
    }

    @Override
    public String toString() {
        return "UndefinedValue{}";
    }
}
