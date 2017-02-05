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

public abstract class Value {
    public enum Type {
        BOOLEAN,
        STRING,
        FUNCTION,
        NUMBER,
        LIST,
        MAP,
        RANGE,
        ITERATOR,
        REFERENCE,
        UNDEFINED,
    }

    private final Type type;

    public Value(Type type) {
        this.type = type;
    }

    public Type getType() {
        return type;
    }

    public boolean asBool() {
        throw new ExecutionException("Can't coerce to boolean");
    }

    abstract public String asString();
    abstract public String asLiteral();

    public BigDecimal asNumber() {
        throw new ExecutionException("Can't coerce to number");
    }

    public Iterator<? extends Value> asIterator() {
        throw new ExecutionException("Can't coerce to iterator");
    }

    public Map<Value, Value> asMap() {
        throw new ExecutionException("Can't coerce to map");
    }

    public List<Value> asList() {
        throw new ExecutionException("Can't coerce to list");
    }

}
