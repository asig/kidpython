package com.asigner.kidpython.compiler.runtime;

import com.asigner.kidpython.compiler.ast.expr.VarNode;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.Map;

public class ReferenceValue extends Value {
    private final VarNode var;

    public ReferenceValue(VarNode var) {
        super(Type.REFERENCE);
        this.var = var;
    }

    public VarNode getVar() {
        return var;
    }

    @Override
    public boolean asBool() {
        throw new ExecutionException("Can't coerce reference to boolean");
    }

    @Override
    public String asString() {
        throw new ExecutionException("Can't coerce reference to string");
    }

    @Override
    public BigDecimal asNumber() {
        throw new ExecutionException("Can't coerce reference to number");
    }

    @Override
    public Iterator<? extends Value> asIterator() {
        throw new ExecutionException("Can't coerce reference to iterator");
    }

    public Map<Value, Value> asMap() {
        throw new ExecutionException("Can't coerce reference to map");
    }

    @Override
    public String toString() {
        return "Reference{" + var.getVar() + "}";
    }
}
