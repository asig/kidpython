// Copyright 2016 Andreas Signer. All rights reserved.

package com.asigner.kidpython.compiler.runtime;

import com.google.common.collect.Maps;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.asigner.kidpython.compiler.runtime.Value.Type.MAP;
import static java.util.stream.Collectors.joining;

public class MapValue extends Value {
    private final Map<Value, Value> mapVal;

    public MapValue(Map<Value, Value> val) {
        super(MAP);
        this.mapVal = val;
    }

    public boolean asBool() {
        return mapVal.size() > 0;
    }

    public String asString() {
        return "{" + mapVal.entrySet().stream().map(e -> e.getKey().asString() + ":" + e.getValue().asString()).collect(joining(",")) + "}";
    }

    public BigDecimal asNumber() {
        return new BigDecimal(mapVal.size());
    }

    @Override
    public Iterator<? extends Value> asIterator() {
        return mapVal.values().iterator();
    }

    public List<Value> asList() {
            return mapVal.values().stream().collect(Collectors.toList());
    }

    public Map<Value, Value> asMap() {
        Map<Value, Value> res = Maps.newHashMap();
        res.putAll(mapVal);
        return res;
    }

    @Override
    public String toString() {
        return "MapValue{" + getType().toString() + ":" + mapVal + "}";
    }

}
