// Copyright 2016 Andreas Signer. All rights reserved.

package com.asigner.kidpython.compiler.runtime;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class FuncValue extends Value {
    private final int startPc;
    private final List<String> params;

    public FuncValue(int startPc, List<String> params) {
        super(Type.FUNCTION);
        this.startPc = startPc;
        this.params = params;
    }

    public int getStartPc() {
        return startPc;
    }

    @Override
    public boolean asBool() {
        return true;
    }

    @Override
    public String asString() {
        return "func@" + startPc;
    }

    @Override
    public BigDecimal asNumber() {
        throw new ExecutionException("Can't coerce function to number");
    }

    @Override
    public Iterator<? extends Value> asIterator() {
        throw new ExecutionException("Can't coerce function to iterator");
    }

    public Map<Value, Value> asMap() {
        throw new ExecutionException("Can't coerce function to map");
    }

    @Override
    public String toString() {
        return "FuncValue{" + getType().toString() + ":" + startPc + "}";
    }

}
