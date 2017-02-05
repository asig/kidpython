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

import com.programmablefun.runtime.UndefinedValue;
import com.programmablefun.runtime.Value;
import com.programmablefun.ide.turtle.TurtleCanvas;
import org.eclipse.swt.graphics.RGB;

import java.util.List;

@Export(name = "turtle")
public class TurtleWrapper {

    private final TurtleCanvas turtleCanvas;

    public TurtleWrapper(TurtleCanvas turtleCanvas) {
        this.turtleCanvas = turtleCanvas;
    }

    @Export
    public Value turn(List<Value> values) {
        NativeCodeUtils.checkArgs(values, 1);
        turtleCanvas.turn(values.get(0).asNumber().doubleValue());
        return UndefinedValue.INSTANCE;
    }

    @Export
    public Value show(List<Value> values) {
        NativeCodeUtils.checkArgs(values, 0);
        turtleCanvas.showTurtle(true);
        return UndefinedValue.INSTANCE;
    }

    @Export
    public Value hide(List<Value> values) {
        NativeCodeUtils.checkArgs(values, 0);
        turtleCanvas.showTurtle(false);
        return UndefinedValue.INSTANCE;
    }

    @Export
    public Value penDown(List<Value> values) {
        NativeCodeUtils.checkArgs(values, 0);
        turtleCanvas.usePen(true);
        return UndefinedValue.INSTANCE;
    }

    @Export
    public Value penColor(List<Value> values) {
        NativeCodeUtils.checkArgs(values, 3);
        turtleCanvas.setPenColor(new RGB(values.get(0).asNumber().intValue(), values.get(1).asNumber().intValue(), values.get(2).asNumber().intValue()));
        return UndefinedValue.INSTANCE;
    }

    @Export
    public Value penWidth(List<Value> values) {
        NativeCodeUtils.checkArgs(values, 1);
        turtleCanvas.setPenWidth(values.get(0).asNumber().intValue());
        return UndefinedValue.INSTANCE;
    }

    @Export
    public Value penUp(List<Value> values) {
        NativeCodeUtils.checkArgs(values, 0);
        turtleCanvas.usePen(false);
        return UndefinedValue.INSTANCE;
    }

    @Export
    public Value move(List<Value> values) {
        NativeCodeUtils.checkArgs(values, 1);
        turtleCanvas.move(values.get(0).asNumber().doubleValue());
        return UndefinedValue.INSTANCE;
    }

    @Export
    public Value home(List<Value> values) {
        NativeCodeUtils.checkArgs(values, 0);
        turtleCanvas.moveTo(0, 0);
        turtleCanvas.turnTo(0);
        return UndefinedValue.INSTANCE;
    }

    @Export
    public Value clear(List<Value> values) {
        NativeCodeUtils.checkArgs(values, 0);
        turtleCanvas.clear();
        return UndefinedValue.INSTANCE;
    }

}
