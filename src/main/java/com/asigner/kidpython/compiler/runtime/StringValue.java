// Copyright 2016 Andreas Signer. All rights reserved.

package com.asigner.kidpython.compiler.runtime;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    public BigDecimal asNumber() {
        return new BigDecimal(strVal);
    }

    @Override
    public Iterator<? extends Value> asIterator() {
        return Stream.of(strVal.toCharArray()).map(c -> new StringValue(String.valueOf(c))).collect(toList()).iterator();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StringValue that = (StringValue) o;
        return Objects.equals(strVal, that.strVal);
    }

    @Override
    public String toString() {
        return "StringValue{" + getType().toString() + ":" + strVal + "}";
    }
}
