package com.asigner.kidpython.runtime;

import com.asigner.kidpython.compiler.ast.expr.VarNode;

import java.util.Objects;

public class VarRefValue extends Value {
    private final String varName;
    private final VarType varType;

    public VarRefValue(VarNode varNode) {
        super(Type.REFERENCE);
        this.varName = varNode.getVarName();
        this.varType = varNode.getVarType();
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
