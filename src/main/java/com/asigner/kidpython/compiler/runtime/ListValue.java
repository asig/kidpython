// Copyright 2016 Andreas Signer. All rights reserved.

package com.asigner.kidpython.compiler.runtime;

import com.google.common.collect.Maps;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static com.asigner.kidpython.compiler.runtime.Value.Type.LIST;
import static java.util.stream.Collectors.joining;

public class ListValue extends Value {
    private final List<Value> listVal;

    public ListValue(List<Value> val) {
        super(LIST);
        this.listVal = val;
    }

    public boolean asBool() {
            return listVal.size() > 0;
    }

    public String asString() {
            return "[" + listVal.stream().map(Value::asString).collect(joining(",")) + "]";
    }

    public BigDecimal asNumber() {
        return new BigDecimal(listVal.size());
    }

    @Override
    public Iterator<? extends Value> asIterator() {
        return listVal.iterator();
    }

    public List<Value> asList() {
return listVal;
    }

    public Map<Value, Value> asMap() {
        Map<Value, Value> res = Maps.newHashMap();
        int i = 0;
        for (Value v : listVal) {
            res.put( new NumberValue(new BigDecimal(i)), v);
        }
        return res;
    }

    @Override
    public String toString() {
        return "ListValue{" + getType().toString() + ":" + listVal + "}";
    }
}
