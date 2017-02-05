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
import java.util.Objects;

import static java.util.stream.Collectors.toList;

public class StringValue extends Value {
    private final String strVal;

    public StringValue(String val) {
        super(Type.STRING);
        this.strVal = val;
    }

    @Override
    public boolean asBool() {
        return Boolean.valueOf(strVal);
    }

    @Override
    public String asString() {
        return strVal;
    }

    @Override
    public String asLiteral() {
        return "\"" + strVal + "\"";
    }

    @Override
    public BigDecimal asNumber() {
        return new BigDecimal(strVal);
    }

    @Override
    public Iterator<? extends Value> asIterator() {
        return strVal.chars()
                .mapToObj(i -> new StringValue(String.valueOf((char)i)))
                .collect(toList())
                .iterator();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StringValue that = (StringValue) o;
        return Objects.equals(strVal, that.strVal);
    }

    @Override
    public int hashCode() {
        return Objects.hash(strVal);
    }

    @Override
    public String toString() {
        return "StringValue{" + getType().toString() + ":" + strVal + "}";
    }
}
