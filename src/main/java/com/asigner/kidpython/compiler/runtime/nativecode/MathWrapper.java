// Copyright 2016 Andreas Signer. All rights reserved.

package com.asigner.kidpython.compiler.runtime.nativecode;

import com.asigner.kidpython.compiler.runtime.NativeFuncValue;
import com.asigner.kidpython.compiler.runtime.NumberValue;
import com.asigner.kidpython.compiler.runtime.Value;
import com.asigner.kidpython.compiler.runtime.VirtualMachine;

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
        frame.setVar("sin", new NativeFuncValue(this::sin));
        frame.setVar("cos", new NativeFuncValue(this::cos));
        frame.setVar("sqrt", new NativeFuncValue(this::sqrt));
    }
}
