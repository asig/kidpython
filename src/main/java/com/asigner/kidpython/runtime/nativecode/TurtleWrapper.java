// Copyright 2016 Andreas Signer. All rights reserved.

package com.asigner.kidpython.runtime.nativecode;

import com.asigner.kidpython.runtime.MapValue;
import com.asigner.kidpython.runtime.NativeFuncValue;
import com.asigner.kidpython.runtime.StringValue;
import com.asigner.kidpython.runtime.UndefinedValue;
import com.asigner.kidpython.runtime.Value;
import com.asigner.kidpython.runtime.VirtualMachine;
import com.asigner.kidpython.ide.turtle.TurtleCanvas;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;

public class TurtleWrapper extends NativeCodeWrapper {

    private final TurtleCanvas turtleCanvas;

    public TurtleWrapper(TurtleCanvas turtleCanvas) {
        this.turtleCanvas = turtleCanvas;
    }

    public Value turn(List<Value> values) {
        checkArgs(values, 1);
        turtleCanvas.turn(values.get(0).asNumber().intValue());
        return UndefinedValue.INSTANCE;
    }

    public Value penDown(List<Value> values) {
        checkArgs(values, 0);
        turtleCanvas.usePen(true);
        return UndefinedValue.INSTANCE;
    }

    public Value penUp(List<Value> values) {
        checkArgs(values, 0);
        turtleCanvas.usePen(false);
        return UndefinedValue.INSTANCE;
    }

    public Value move(List<Value> values) {
        checkArgs(values, 1);
        turtleCanvas.move(values.get(0).asNumber().doubleValue());
        return UndefinedValue.INSTANCE;
    }

    public Value home(List<Value> values) {
        checkArgs(values, 0);
        turtleCanvas.moveTo(0, 0);
        turtleCanvas.turnTo(0);
        return UndefinedValue.INSTANCE;
    }

    @Override
    public void registerWith(VirtualMachine.Frame frame) {
        Map<Value, Value> turtle = Maps.newHashMap();
        turtle.put(new StringValue("turn"), new NativeFuncValue(this::turn));
        turtle.put(new StringValue("penDown"), new NativeFuncValue(this::penDown));
        turtle.put(new StringValue("penUp"), new NativeFuncValue(this::penUp));
        turtle.put(new StringValue("move"), new NativeFuncValue(this::move));
        turtle.put(new StringValue("home"), new NativeFuncValue(this::home));

        frame.setVar("turtle", new MapValue(turtle));
    }

    @Override
    public List<String> getExposedNames() {
        return Lists.newArrayList("turtle", "turn", "penDown", "penUp", "move", "home");
    }

}