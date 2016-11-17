package com.asigner.kidpython.runtime;

import java.util.Objects;

public class VarRefValue extends Value {
    private final String varName;
    private final VarType varType;

    public VarRefValue(String varName, VarType varType) {
        super(Type.REFERENCE);
        this.varName = varName;
        this.varType = varType;
    }

    public String getVarName() {
        return varName;
    }

    public VarType getVarType() {
        return varType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VarRefValue that = (VarRefValue) o;
        return Objects.equals(varName, that.varName) &&
                varType == that.varType;
    }

    @Override
    public String asString() {
        return "reference to " + varName;
    }

    @Override
    public String asLiteral() {
        return "reference to " + varName;
    }

    @Override
    public int hashCode() {
        return Objects.hash(varName, varType);
    }

    @Override
    public String toString() {
        return "VarRefValue{" + varName + "(" + varType + ")}";
    }
}
