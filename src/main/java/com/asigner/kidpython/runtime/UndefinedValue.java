package com.asigner.kidpython.runtime;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class UndefinedValue extends Value {
    public static final UndefinedValue INSTANCE = new UndefinedValue();

    private UndefinedValue() {
        super(Type.UNDEFINED);
    }

    @Override
    public boolean asBool() {
        throw new RuntimeException("Can't coerce undefined to a boolean");
    }

    @Override
    public String asString() {
        throw new ExecutionException("Can't coerce undefined to a string");
    }

    @Override
    public BigDecimal asNumber() {
        throw new RuntimeException("Can't coerce undefined to a number");
    }

    @Override
    public Iterator<? extends Value> asIterator() {
        throw new ExecutionException("Can't coerce undefined to an iterator");
    };

    @Override
    public List<Value> asList() {
        throw new RuntimeException("Can't coerce undefined to a list");
    }

    @Override
    public Map<Value, Value> asMap() {
        throw new RuntimeException("Can't coerce undefined to a map");
    }

    @Override
    public String toString() {
        return "UndefinedValue{}";
    }
}
