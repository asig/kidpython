// Copyright 2016 Andreas Signer. All rights reserved.

package com.asigner.kidpython.runtime.nativecode;

import com.asigner.kidpython.runtime.NativeFuncValue;
import com.asigner.kidpython.runtime.NumberValue;
import com.asigner.kidpython.runtime.Value;
import com.asigner.kidpython.runtime.VarType;
import com.asigner.kidpython.runtime.VirtualMachine;
import com.google.common.collect.Lists;

import java.math.BigDecimal;
import java.util.List;

public class MathWrapper extends NativeCodeWrapper {

    public Value sin(List<Value> values) {
        checkArgs(values, 1);
        return new NumberValue(new BigDecimal(Math.sin(values.get(0).asNumber().doubleValue())));
    }

    public Value cos(List<Value> values) {
        checkArgs(values, 1);
        return new NumberValue(new BigDecimal(Math.cos(values.get(0).asNumber().doubleValue())));
    }

    public Value sqrt(List<Value> values) {
        checkArgs(values, 1);
        return new NumberValue(new BigDecimal(Math.sqrt(values.get(0).asNumber().doubleValue())));
    }

    @Override
    public void registerWith(VirtualMachine.Frame frame) {
        frame.setVar("sin", VarType.SYSTEM, new NativeFuncValue(this::sin));
        frame.setVar("cos", VarType.SYSTEM, new NativeFuncValue(this::cos));
        frame.setVar("sqrt", VarType.SYSTEM, new NativeFuncValue(this::sqrt));
    }

    @Override
    public List<String> getExposedNames() {
        return Lists.newArrayList("sin", "cos", "sqrt");
    }

}
