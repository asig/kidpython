// Copyright 2016 Andreas Signer. All rights reserved.

package com.asigner.kidpython.compiler.runtime;

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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NumberValue that = (NumberValue) o;
        return Objects.equals(numVal, that.numVal);
    }

    @Override
    public String toString() {
        return "NumberValue{" + getType().toString() + ":" + numVal + "}";
    }
}
