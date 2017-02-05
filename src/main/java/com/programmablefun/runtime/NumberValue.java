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
import java.util.Objects;

public class NumberValue extends Value {
    private final BigDecimal numVal;

    public NumberValue(BigDecimal val) {
        super(Type.NUMBER);
        this.numVal = val;
    }

    @Override
    public boolean asBool() {
        return numVal.compareTo(BigDecimal.ZERO) != 0;
    }

    @Override
    public String asString() {
        return numVal.toString();
    }

    @Override
    public BigDecimal asNumber() {
        return numVal;
    }

    @Override
    public String asLiteral() {
        return numVal.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NumberValue that = (NumberValue) o;
        return Objects.equals(numVal, that.numVal);
    }

    @Override
    public int hashCode() {
        return Objects.hash(numVal);
    }

    @Override
    public String toString() {
        return "NumberValue{" + getType().toString() + ":" + numVal + "}";
    }
}
