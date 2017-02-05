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
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.programmablefun.runtime.Value.Type.MAP;
import static java.util.stream.Collectors.joining;

public class MapValue extends Value {
    private final Map<Value, Value> mapVal;

    public MapValue(Map<Value, Value> val) {
        super(MAP);
        this.mapVal = val;
    }

    @Override
    public Iterator<? extends Value> asIterator() {
        return mapVal.values().iterator();
    }

    public List<Value> asList() {
            return mapVal.values().stream().collect(Collectors.toList());
    }

    public Map<Value, Value> asMap() {
        return mapVal;
    }

    @Override
    public String asString() {
        return "{" + mapVal.entrySet().stream().map(e -> e.getKey().asString() + ": " + e.getValue().asString()).collect(joining(", ")) + "}";
    }

    @Override
    public String asLiteral() {
        return "{" + mapVal.entrySet().stream().map(e -> e.getKey().asLiteral() + ": " + e.getValue().asLiteral()).collect(joining(", ")) + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MapValue mapValue = (MapValue) o;

        if (this.mapVal.size() != mapValue.mapVal.size()) {
            return false;
        }
        for (Value key : this.mapVal.keySet()) {
            if (!Objects.equals(this.mapVal.get(key), mapValue.mapVal.get(key))) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(mapVal);
    }

    @Override
    public String toString() {
        return "MapValue{" + getType().toString() + ":" + mapVal + "}";
    }

}
