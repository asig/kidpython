// Copyright 2016 Andreas Signer. All rights reserved.

package com.asigner.kidpython.compiler.runtime;

import com.google.common.collect.Maps;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
    public String toString() {
        return "NativeFuncValue{" + getType().toString() + ":" + func + "}";
    }

}
