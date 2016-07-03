package com.asigner.kidpython.compiler.runtime;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.joining;

public abstract class Value {
    public enum Type {
        BOOLEAN,
        STRING,
        FUNCTION,
        NUMBER,
        LIST,
        MAP,
        ITERATOR,
    }

    private final Type type;

    public Value(Type type) {
        this.type = type;
    }

    public Type getType() {
        return type;
    }

    public abstract boolean asBool();
    public abstract String asString();
    public abstract BigDecimal asNumber();

    public Iterator<? extends Value> asIterator() {
        return Lists.newArrayList(this).iterator();
    };

    public List<Value> asList() {
        return Lists.newArrayList(this);
    }

    public Map<Value, Value> asMap() {
        Map<Value, Value> res = Maps.newHashMap();
        res.put(new NumberValue(BigDecimal.ZERO), this);
        return res;
    }
}
