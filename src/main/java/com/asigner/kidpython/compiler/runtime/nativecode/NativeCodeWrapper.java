// Copyright 2016 Andreas Signer. All rights reserved.

package com.asigner.kidpython.compiler.runtime.nativecode;

import com.asigner.kidpython.compiler.runtime.ExecutionException;
import com.asigner.kidpython.compiler.runtime.Value;
import com.asigner.kidpython.compiler.runtime.VirtualMachine;

import java.util.List;

public abstract class NativeCodeWrapper {

    protected void checkArgs(List<Value> values, int count) {
        if (values.size() != count) {
            throw new ExecutionException("Must pass exactly " + count + " values");
        }
    }

    public abstract void registerWith(VirtualMachine.Frame frame);
}
