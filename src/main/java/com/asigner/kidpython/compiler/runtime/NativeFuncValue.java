// Copyright 2016 Andreas Signer. All rights reserved.

package com.asigner.kidpython.compiler.runtime;

import com.asigner.kidpython.compiler.ast.Stmt;
import com.google.common.collect.Maps;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class NativeFuncValue  extends Value {

    public interface Iface {
        Value run(List<Value> vals);
    }

    private final Iface func;

    public NativeFuncValue(Iface func) {
        super(Type.FUNCTION);
        this.func = func;
    }

    @Override
    public boolean asBool() {
        return true;
    }

    @Override
    public String asString() {
        return "func@" + func.hashCode();
    }

    @Override
    public BigDecimal asNumber() {
        return new BigDecimal(func.hashCode());
    }

    @Override
    public Iterator<? extends Value> asIterator() {
        return null;
    }

    public Map<Value, Value> asMap() {
        Map<Value, Value> res = Maps.newHashMap();
        res.put(new NumberValue(new BigDecimal(func.hashCode())), this);
        return res;
    }

    @Override
    public String toString() {
        return "NativeFuncValue{" + getType().toString() + ":" + func + "}";
    }

}
