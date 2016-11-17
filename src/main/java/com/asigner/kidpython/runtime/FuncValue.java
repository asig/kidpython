// Copyright 2016 Andreas Signer. All rights reserved.

package com.asigner.kidpython.runtime;

import java.util.List;
import java.util.Objects;

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

    public List<String> getParams() {
        return params;
    }

    @Override
    public String asString() {
        return "function";
    }

    @Override
    public String asLiteral() {
        return "function";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FuncValue funcValue = (FuncValue) o;

        if (startPc != funcValue.startPc) return false;
        return params != null ? params.equals(funcValue.params) : funcValue.params == null;

    }

    @Override
    public int hashCode() {
        return Objects.hash(startPc, params);
    }

    @Override
    public String toString() {
        return "FuncValue{" + getType().toString() + ":" + startPc + "}";
    }

}
