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

import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import static com.programmablefun.runtime.Value.Type.LIST;
import static java.util.stream.Collectors.joining;

public class ListValue extends Value {
    private final List<Value> listVal;

    public ListValue(List<Value> val) {
        super(LIST);
        this.listVal = val;
    }

    @Override
    public Iterator<? extends Value> asIterator() {
        return listVal.iterator();
    }

    public List<Value> asList() {
        return listVal;
    }

    @Override
    public String asString() {
        return "[" + listVal.stream().map(Value::asString).collect(joining(", ")) + "]";
    }

    @Override
    public String asLiteral() {
        return "[" + listVal.stream().map(Value::asLiteral).collect(joining(", ")) + "]";
    }

    @Override
    public String toString() {
        return "ListValue{" + getType().toString() + ":" + listVal + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ListValue listValue = (ListValue) o;

        if (this.listVal.size() != listValue.listVal.size()) {
            return false;
        }
        Iterator<Value> i1 = this.listVal.iterator();
        Iterator<Value> i2 = listValue.listVal.iterator();
        while (i1.hasNext()) {
            if (!(i1.next().equals(i2.next()))) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(listVal);
    }
}
