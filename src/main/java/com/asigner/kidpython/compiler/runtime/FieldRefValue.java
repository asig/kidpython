// Copyright 2016 Andreas Signer. All rights reserved.

package com.asigner.kidpython.compiler.runtime;

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
