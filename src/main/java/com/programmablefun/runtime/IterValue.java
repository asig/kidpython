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
import java.util.Objects;

import static com.programmablefun.runtime.Value.Type.ITERATOR;

public class IterValue extends Value {
    private final Iterator<? extends Value> iterVal;

    public IterValue(Iterator<? extends Value> val) {
        super(ITERATOR);
        this.iterVal = val;
    }

    public boolean asBool() {
        return iterVal.hasNext();
    }

    public String asString() {
        return "iter";
    }

    @Override
    public String asLiteral() {
        return "iter";
    }

    public BigDecimal asNumber() {
        throw new IllegalStateException();
    }

    public Iterator<? extends Value> asIterator() {
        throw new IllegalStateException();
    }

    public List<Value> asList() {
        throw new IllegalStateException();
    }

    public Map<Value, Value> asMap() {
        throw new IllegalStateException();
    }

    public Iterator<? extends Value> getIterator() {
        return iterVal;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IterValue iterValue = (IterValue) o;
        return Objects.equals(iterVal, iterValue.iterVal);
    }

    @Override
    public int hashCode() {
        return Objects.hash(iterVal);
    }

    @Override
    public String toString() {
        return "IterValue{" + getType().toString() + ":" + iterVal + "}";
    }

}
