/*
 * Copyright (c) 2018 Andreas Signer <asigner@gmail.com>
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

import com.programmablefun.ide.turtle.Canvas;
import com.programmablefun.runtime.UndefinedValue;
import com.programmablefun.runtime.Value;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;

import java.util.List;

@Export(name = "canvas")
public class CanvasWrapper {

    private final Canvas canvas;

    private RGB color;

    public CanvasWrapper(Canvas canvas) {
        this.canvas = canvas;
    }

    @Export
    public Value color(List<Value> values) {
        NativeCodeUtils.checkArgs(values, 3);
        this.color = new RGB(values.get(0).asNumber().intValue(), values.get(1).asNumber().intValue(), values.get(2).asNumber().intValue());
        return UndefinedValue.INSTANCE;
    }

    @Export
    public Value clear(List<Value> values) {
        NativeCodeUtils.checkArgs(values, 0);
        canvas.clear();
        return UndefinedValue.INSTANCE;
    }

    @Export
    public Value fillRect(List<Value> values) {
        NativeCodeUtils.checkArgs(values, 4);
        double x = values.get(0).asNumber().doubleValue();
        double y = values.get(1).asNumber().doubleValue();
        double w = values.get(2).asNumber().doubleValue();
        double h = values.get(3).asNumber().doubleValue();
        canvas.addShape(new Canvas.FilledRect(new Point((int)(x+.5),(int)(y+.5)), (int)(w+.5), (int)(h+.5), color));
        canvas.redrawAsync();
        return UndefinedValue.INSTANCE;
    }
}
