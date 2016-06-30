package com.asigner.kidpython.compiler.runtime;

import com.asigner.kidpython.compiler.ast.Node;
import com.google.common.base.Preconditions;

import java.math.BigDecimal;

public class Value {
    enum Type {
        BOOLEAN,
        STRING,
        FUNCTION,
        NUMBER
    }

    private final Type type;
    private final Boolean boolVal;
    private final String strVal;
    private final BigDecimal numVal;
    private final Node funcVal;

    public static Value of(Boolean val) {
        return new Value(Type.BOOLEAN, val, null, null, null);
    }

    public static Value of(BigDecimal val) {
        return new Value(Type.NUMBER, null, val, null, null);
    }

    public static Value of(String val) {
        return new Value(Type.STRING, null, null, val, null);
    }

    public static Value of(Node val) {
        return new Value(Type.FUNCTION, null, null, null, val);
    }

    private Value(Type t, Boolean boolVal, BigDecimal numVal, String strVal, Node funcVal) {
        this.type = t;
        this.boolVal = boolVal;
        this.numVal = numVal;
        this.strVal = strVal;
        this.funcVal = funcVal;
        Preconditions.checkState(
                (boolVal != null ? 1 : 0) +
                (numVal != null ? 1 : 0) +
                (strVal != null ? 1 : 0) +
                (funcVal != null ? 1 : 0) == 1);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Value{").append(type.toString()).append(":");
        switch (type) {
            case BOOLEAN:
                builder.append(boolVal.toString());
                break;
            case STRING:
                builder.append(strVal);
                break;
            case FUNCTION:
                builder.append(funcVal.toString());
                break;
            case NUMBER:
                builder.append(numVal.toString());
                break;
        }
        return builder.toString();
    }
}
