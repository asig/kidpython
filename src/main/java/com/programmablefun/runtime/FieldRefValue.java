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

import java.util.Objects;

public class FieldRefValue extends Value {
    private final MapValue map;
    private final Value key;

    public FieldRefValue(MapValue map, Value key) {
        super(Value.Type.REFERENCE);
        this.map = map;
        this.key = key;
    }

    public Value getKey() {
        return key;
    }

    public MapValue getMap() {
        return map;
    }

    @Override
    public String asString() {
        return "field " + key.asString();
    }

    @Override
    public String asLiteral() {
        return "." + key.asLiteral();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FieldRefValue that = (FieldRefValue) o;
        return Objects.equals(map, that.map) &&
                Objects.equals(key, that.key);
    }

    @Override
    public int hashCode() {
        return Objects.hash(map, key);
    }
}
