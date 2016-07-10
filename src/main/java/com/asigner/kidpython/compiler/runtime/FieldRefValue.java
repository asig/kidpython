// Copyright 2016 Andreas Signer. All rights reserved.

package com.asigner.kidpython.compiler.runtime;

import com.asigner.kidpython.compiler.ast.expr.ExprNode;
import com.asigner.kidpython.compiler.ast.expr.VarNode;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.Map;

public class FieldRefValue extends Value {
    private final MapValue map;
    private final Value key;

    public FieldRefValue(MapValue map, Value key) {
        super(Value.Type.REFERENCE);
        this.map = map;
        this.key = key;
    }

    public Value getKey() {
        return key;
    }

    public MapValue getMap() {
        return map;
    }
}
