// Copyright 2016 Andreas Signer. All rights reserved.

package com.asigner.kidpython.compiler.runtime;

import com.asigner.kidpython.compiler.ast.Node;
import com.asigner.kidpython.compiler.ast.Stmt;
import com.google.common.collect.Maps;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.joining;

public class FuncValue extends Value {
    private final Stmt funcVal;
    private final List<String> params;

    public FuncValue(Stmt val, List<String> params) {
        super(Type.FUNCTION);
        this.funcVal = val;
        this.params = params;
    }

    public Stmt getValue() {
        return funcVal;
    }

    @Override
    public boolean asBool() {
        return true;
    }

    @Override
    public String asString() {
        return "func@" + funcVal.hashCode();
    }

    @Override
    public BigDecimal asNumber() {
        return new BigDecimal(funcVal.hashCode());
    }

    @Override
    public Iterator<? extends Value> asIterator() {
        return null;
    }

    public Map<Value, Value> asMap() {
        Map<Value, Value> res = Maps.newHashMap();
        res.put(new NumberValue(new BigDecimal(funcVal.hashCode())), this);
        return res;
    }

    @Override
    public String toString() {
        return "FuncValue{" + getType().toString() + ":" + funcVal + "}";
    }

}
