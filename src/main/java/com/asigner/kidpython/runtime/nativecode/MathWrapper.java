// Copyright 2016 Andreas Signer. All rights reserved.

package com.asigner.kidpython.runtime.nativecode;

import com.asigner.kidpython.runtime.NumberValue;
import com.asigner.kidpython.runtime.Value;

import java.math.BigDecimal;
import java.util.List;

public class MathWrapper {

    @Export
    public static Value sin(List<Value> values) {
        NativeCodeUtils.checkArgs(values, 1);
        return new NumberValue(new BigDecimal(Math.sin(values.get(0).asNumber().doubleValue())));
    }

    @Export
    public static Value cos(List<Value> values) {
        NativeCodeUtils.checkArgs(values, 1);
        return new NumberValue(new BigDecimal(Math.cos(values.get(0).asNumber().doubleValue())));
    }

    @Export
    public static Value sqrt(List<Value> values) {
        NativeCodeUtils.checkArgs(values, 1);
        return new NumberValue(new BigDecimal(Math.sqrt(values.get(0).asNumber().doubleValue())));
    }
}
