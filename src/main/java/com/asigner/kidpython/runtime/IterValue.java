// Copyright 2016 Andreas Signer. All rights reserved.

package com.asigner.kidpython.runtime;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.asigner.kidpython.runtime.Value.Type.ITERATOR;

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
