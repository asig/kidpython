// Copyright 2016 Andreas Signer. All rights reserved.

package com.asigner.kidpython.runtime.nativecode;

import com.asigner.kidpython.runtime.ExecutionException;
import com.asigner.kidpython.runtime.NativeFuncValue;
import com.asigner.kidpython.runtime.NumberValue;
import com.asigner.kidpython.runtime.StringValue;
import com.asigner.kidpython.runtime.UndefinedValue;
import com.asigner.kidpython.runtime.Value;
import com.asigner.kidpython.runtime.VirtualMachine;
import com.asigner.kidpython.ide.console.ConsoleComposite;
import com.google.common.collect.Lists;
import org.eclipse.swt.widgets.Display;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.util.List;

public class UtilsWrapper extends NativeCodeWrapper {

    private final PrintStream stdout;
    private final InputStream stdin;
    private final ConsoleComposite consoleComposite;

    public UtilsWrapper(ConsoleComposite consoleComposite) {
        this.consoleComposite = consoleComposite;
        this.stdin = consoleComposite.getInputStream();
        this.stdout = new PrintStream(consoleComposite.getOutputStream());
    }

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

    public Value println(List<Value> values) {
        print(values);
        stdout.println();
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

    @Override
    public void registerWith(VirtualMachine.Frame frame) {
        frame.setVar("print", new NativeFuncValue(this::print));
        frame.setVar("println", new NativeFuncValue(this::println));
        frame.setVar("input", new NativeFuncValue(this::input));
        frame.setVar("len", new NativeFuncValue(this::utilsLen));
    }

    @Override
    public List<String> getExposedNames() {
        return Lists.newArrayList("print", "println", "input", "len");
    }
}
