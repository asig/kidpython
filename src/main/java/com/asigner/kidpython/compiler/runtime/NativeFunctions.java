package com.asigner.kidpython.compiler.runtime;

import com.asigner.kidpython.ide.turtle.TurtleCanvas;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.List;

public class NativeFunctions {

    private final PrintStream stdout;
    private final InputStream stdin;
    private final TurtleCanvas turtle;

    public NativeFunctions(InputStream stdin, OutputStream stdout, TurtleCanvas turtle) {
        this.stdin = stdin;
        this.stdout = new PrintStream(stdout);
        this.turtle = turtle;
    }

    public Value print(List<Value> values) {
        for (Value v : values) {
            stdout.print(v.asString());
        }
        stdout.flush();
        return UndefinedValue.INSTANCE;
    }

    public Value turtleTurn(List<Value> values) {
        turtle.turn(values.get(0).asNumber().intValue());
        return UndefinedValue.INSTANCE;
    }

    public Value turtlePenDown(List<Value> values) {
        turtle.usePen(true);
        return UndefinedValue.INSTANCE;
    }

    public Value turtlePenUp(List<Value> values) {
        turtle.usePen(false);
        return UndefinedValue.INSTANCE;
    }

    public Value turtleMove(List<Value> values) {
        turtle.move(values.get(0).asNumber().doubleValue());
        return UndefinedValue.INSTANCE;
    }
}
