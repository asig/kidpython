package com.asigner.kidpython.compiler.runtime;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public abstract class Value {
    public enum Type {
        BOOLEAN,
        STRING,
        FUNCTION,
        NUMBER,
        LIST,
        MAP,
        ITERATOR,
        REFERENCE,
        UNDEFINED,
    }

    private final Type type;

    public Value(Type type) {
        this.type = type;
    }

    public Type getType() {
        return type;
    }

    public boolean asBool() {
        throw new ExecutionException("Can't coerce to boolean");
    }

    public String asString() {
        throw new ExecutionException("Can't coerce to string");
    }

    public BigDecimal asNumber() {
        throw new ExecutionException("Can't coerce to number");
    }

    public Iterator<? extends Value> asIterator() {
        throw new ExecutionException("Can't coerce to iterator");
    }

    public Map<Value, Value> asMap() {
        throw new ExecutionException("Can't coerce to map");
    }

    public List<Value> asList() {
        throw new ExecutionException("Can't coerce to list");
    }

}
