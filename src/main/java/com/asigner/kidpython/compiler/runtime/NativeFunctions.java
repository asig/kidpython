package com.asigner.kidpython.compiler.runtime;

import com.asigner.kidpython.ide.console.ConsoleComposite;
import com.asigner.kidpython.ide.turtle.TurtleCanvas;
import org.eclipse.swt.widgets.Display;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.util.List;

public class NativeFunctions {

    private final PrintStream stdout;
    private final InputStream stdin;
    private final TurtleCanvas turtle;
    private final ConsoleComposite consoleComposite;

    public NativeFunctions(TurtleCanvas turtle, ConsoleComposite consoleComposite) {
        this.consoleComposite = consoleComposite;
        this.stdin = consoleComposite.getInputStream();
        this.stdout = new PrintStream(consoleComposite.getOutputStream());
        this.turtle = turtle;
    }

    // =========================
    // Input/Output

    public Value print(List<Value> values) {
        for (Value v : values) {
            String s = v.asString()
                    .replaceAll("\\\\n", "\n")
                    .replaceAll("\\\\r", "\r")
                    .replaceAll("\\\\b", "\b")
                    .replaceAll("\\\\t", "\t")
                    .replaceAll("\\\\f", "\f");
            stdout.print(s);
        }
        stdout.flush();
        return UndefinedValue.INSTANCE;
    }

    public Value input(List<Value> values) {
        print(values);
        Display.getDefault().syncExec(consoleComposite::forceFocus);
        try {
            while(stdin.available() > 0) {
                stdin.read();
            }
            StringBuffer res = new StringBuffer();
            for(;;) {
                int c = stdin.read();
                if (c == -1 || c == '\n' | c == '\r') {
                    break;
                }
                res.append((char)c);
            }
            return new StringValue(res.toString());
        } catch (IOException e) {
            throw new ExecutionException("Can't read from stdin!");
        }
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
