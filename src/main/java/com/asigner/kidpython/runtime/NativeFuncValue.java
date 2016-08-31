// Copyright 2016 Andreas Signer. All rights reserved.

package com.asigner.kidpython.runtime;

import java.util.List;
import java.util.Objects;

public class NativeFuncValue extends Value {

    public interface Iface {
        Value run(List<Value> vals);
    }

    private final Iface func;

    public NativeFuncValue(Iface func) {
        super(Type.FUNCTION);
        this.func = func;
    }

    public Iface getFunc() {
        return func;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NativeFuncValue that = (NativeFuncValue) o;
        return Objects.equals(func, that.func);
    }

    @Override
    public int hashCode() {
        return Objects.hash(func);
    }

    @Override
    public String toString() {
        return "NativeFuncValue{" + getType().toString() + ":" + func + "}";
    }

}
