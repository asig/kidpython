package com.asigner.kidpython.compiler.runtime;

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
    public String toString() {
        return "VarRefValue{" + var + "}";
    }
}
