// Copyright 2016 Andreas Signer. All rights reserved.

package com.asigner.kidpython.compiler.runtime;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.Objects;

public class BooleanValue extends Value {
    private final Boolean boolVal;

    public BooleanValue(Boolean val) {
        super(Type.BOOLEAN);
        this.boolVal = val;
    }

    @Override
    public boolean asBool() {
        return boolVal;
    }

    @Override
    public String asString() {
        return String.valueOf(boolVal);
    }

    @Override
    public BigDecimal asNumber() {
        return boolVal ? BigDecimal.ONE : BigDecimal.ZERO;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BooleanValue that = (BooleanValue) o;
        return Objects.equals(boolVal, that.boolVal);
    }

    @Override
    public String toString() {
        return "BooleanValue{" + getType().toString() + ":" + boolVal + "}";
    }
}
