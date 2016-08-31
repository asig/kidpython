package com.asigner.kidpython.runtime;

import java.util.Objects;

public class VarRefValue extends Value {
    private final String var;

    public VarRefValue(String var) {
        super(Type.REFERENCE);
        this.var = var;
    }

    public String getVar() {
        return var;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VarRefValue that = (VarRefValue) o;
        return Objects.equals(var, that.var);
    }

    @Override
    public int hashCode() {
        return Objects.hash(var);
    }

    @Override
    public String toString() {
        return "VarRefValue{" + var + "}";
    }
}
