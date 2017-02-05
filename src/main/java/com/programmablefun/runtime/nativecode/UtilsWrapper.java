/*
 * Copyright (c) 2017 Andreas Signer <asigner@gmail.com>
 *
 * This file is part of programmablefun.
 *
 * programmablefun is free software: you can redistribute it and/or
 * modify it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * programmablefun is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with programmablefun.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.programmablefun.runtime.nativecode;

import com.programmablefun.runtime.ExecutionException;
import com.programmablefun.runtime.NumberValue;
import com.programmablefun.runtime.StringValue;
import com.programmablefun.runtime.UndefinedValue;
import com.programmablefun.runtime.Value;
import com.programmablefun.ide.console.ConsoleComposite;
import org.eclipse.swt.widgets.Display;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.util.List;

public class UtilsWrapper {

    private final PrintStream stdout;
    private final InputStream stdin;
    private final ConsoleComposite consoleComposite;

    public UtilsWrapper(ConsoleComposite consoleComposite) {
        this.consoleComposite = consoleComposite;
        this.stdin = consoleComposite.getInputStream();
        this.stdout = new PrintStream(consoleComposite.getOutputStream());
    }

    @Export
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

    @Export
    public Value println(List<Value> values) {
        print(values);
        stdout.println();
        stdout.flush();
        return UndefinedValue.INSTANCE;
    }

    @Export
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

    @Export(name="len")
    public Value utilsLen(List<Value> values) {
        NativeCodeUtils.checkArgs(values, 1);
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

    @Export(name="wait")
    public Value utilsWait(List<Value> values) {
        NativeCodeUtils.checkArgs(values, 1);
        int delay = values.get(0).asNumber().multiply(new BigDecimal(1000)).intValue();
        try {
            Thread.sleep(delay);
        } catch (InterruptedException ignored) {
        }
        return UndefinedValue.INSTANCE;
    }
}
