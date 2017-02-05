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

package com.programmablefun.runtime.nativecode;

import com.programmablefun.runtime.NumberValue;
import com.programmablefun.runtime.Value;

import java.math.BigDecimal;
import java.util.List;

public class MathWrapper {

    @Export
    public static Value sin(List<Value> values) {
        NativeCodeUtils.checkArgs(values, 1);
        return new NumberValue(new BigDecimal(Math.sin(values.get(0).asNumber().doubleValue())));
    }

    @Export
    public static Value cos(List<Value> values) {
        NativeCodeUtils.checkArgs(values, 1);
        return new NumberValue(new BigDecimal(Math.cos(values.get(0).asNumber().doubleValue())));
    }

    @Export
    public static Value sqrt(List<Value> values) {
        NativeCodeUtils.checkArgs(values, 1);
        return new NumberValue(new BigDecimal(Math.sqrt(values.get(0).asNumber().doubleValue())));
    }
}
