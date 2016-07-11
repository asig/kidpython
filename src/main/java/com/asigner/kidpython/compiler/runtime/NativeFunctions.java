package com.asigner.kidpython.compiler.runtime;

import com.asigner.kidpython.ide.turtle.TurtleCanvas;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.math.BigDecimal;
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

    // =========================
    // Input/Output

    public Value print(List<Value> values) {
        for (Value v : values) {
            stdout.print(v.asString());
        }
        stdout.flush();
        return UndefinedValue.INSTANCE;
    }

    // Input/Output
    // =========================

    // =========================
    // Utils

    public Value utilsLen(List<Value> values) {
        checkArgs(values, 1);
        Value v = values.get(0);
        switch(v.getType()) {
            case STRING:
                return new NumberValue(new BigDecimal(v.asString().length()));
            case LIST:
                return new NumberValue(new BigDecimal(v.asList().size()));
            case MAP:
                return new NumberValue(new BigDecimal(v.asMap().size()));
            default:
                throw new ExecutionException("Can't take length of " + v.getType());
        }
    }

    // Utils
    // =========================

    // Input/Output
    // =========================

    // =========================
    // Turtle

    public Value turtleTurn(List<Value> values) {
        checkArgs(values, 1);
        turtle.turn(values.get(0).asNumber().intValue());
        return UndefinedValue.INSTANCE;
    }

    public Value turtlePenDown(List<Value> values) {
        checkArgs(values, 0);
        turtle.usePen(true);
        return UndefinedValue.INSTANCE;
    }

    public Value turtlePenUp(List<Value> values) {
        checkArgs(values, 0);
        turtle.usePen(false);
        return UndefinedValue.INSTANCE;
    }

    public Value turtleMove(List<Value> values) {
        checkArgs(values, 1);
        turtle.move(values.get(0).asNumber().doubleValue());
        return UndefinedValue.INSTANCE;
    }

    // Turtle
    // =========================

    // =========================
    // Math

    public Value mathSin(List<Value> values) {
        checkArgs(values, 1);
        return new NumberValue(new BigDecimal(Math.sin(values.get(0).asNumber().doubleValue())));
    }

    public Value mathCos(List<Value> values) {
        checkArgs(values, 1);
        return new NumberValue(new BigDecimal(Math.cos(values.get(0).asNumber().doubleValue())));
    }

    // Math
    // =========================

    private void checkArgs(List<Value> values, int count) {
        if (values.size() != count) {
            throw new ExecutionException("Must pass exactly " + count + " values");
        }

    }
}
