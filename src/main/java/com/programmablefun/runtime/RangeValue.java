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

import com.google.common.collect.Lists;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import static com.programmablefun.runtime.Value.Type.RANGE;

public class RangeValue extends Value {

    private class RangeIterator implements Iterator<Value> {

        private BigDecimal cur = start.asNumber().subtract(BigDecimal.ONE);

        @Override
        public boolean hasNext() {
            return cur.compareTo(end.asNumber()) < 0;
        }

        @Override
        public Value next() {
            cur = cur.add(BigDecimal.ONE);
            return new NumberValue(cur);
        }
    }

    private Value start;
    private Value end;

    public RangeValue(Value start, Value end) {
        super(RANGE);
        if (!(start instanceof NumberValue)) {
            throw new ExecutionException("range start must be a number");
        }
        this.start = start;
        if (!(end instanceof NumberValue)) {
            throw new ExecutionException("range end must be a number");
        }
        this.end = end;
    }

    @Override
    public Iterator<? extends Value> asIterator() {
        return new RangeIterator();
    }

    public List<Value> asList() {
        List<Value> res = Lists.newLinkedList();
        RangeIterator iter = new RangeIterator();
        while (iter.hasNext()) {
            res.add(iter.next());
        }
        return res;
    }

    @Override
    public String asString() {
        return start.asString() + " .. " + end.asString();
    }

    @Override
    public String asLiteral() {
        return start.asLiteral() + " .. " + end.asLiteral();
    }

    @Override
    public String toString() {
        return "RangeValue{" + getType().toString() + ": start=" + start + "; end=" + end + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RangeValue that = (RangeValue) o;
        return Objects.equals(start, that.start) &&
                Objects.equals(end, that.end);
    }

    public boolean contains(Value v) {
        BigDecimal num = v.asNumber();
        return start.asNumber().compareTo(num) <= 0 && num.compareTo(end.asNumber()) <= 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(start, end);
    }
}
