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

package com.programmablefun.ide.turtle;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;

public class Turtle {

    private final TurtleCanvas canvas;

    private double angle;
    private double posX, posY;
    private boolean penDown;
    private boolean turtleVisible;
    private RGB penColor;
    private int penWidth;
    private boolean slowMotion = false;

    public Turtle(TurtleCanvas canvas) {
        this.canvas = canvas;
    }

    public void reset() {
        posX = posY = angle = 0;
        penDown = true;
        penColor = new RGB(0,0,0);
        penWidth = 1;
        turtleVisible = true;
        this.slowMotion = false;
    }

    public void turnTo(double angle) {
        this.angle = angle;
        if (turtleVisible) {
            canvas.redrawAsync();
        }
    }

    public void moveTo(double x, double y) {
        this.posX = x;
        this.posY = y;
        if (turtleVisible) {
            canvas.redrawAsync();
        }
    }

    public void move(double len) {
        if (len <= 0) {
            return;
        }
        double startLen;
        int delay;
        if (slowMotion) {
            startLen = 1;
            delay = 4;
        } else {
            startLen = len;
            delay = 0;
        }

        if (penDown) {
            Point from = new Point(0,0);
            Point to = new Point(0,0);
            canvas.addLine(new TurtleCanvas.Line(from, to, penColor,penWidth));
        }
        double origPosX = posX;
        double origPosY = posY;
        for (double i = startLen; i <= len; i ++) {
            posX = origPosX;
            posY = origPosY;
            double newPosX = posX + Math.sin(Math.toRadians(angle)) * i;
            double newPosY = posY - Math.cos(Math.toRadians(angle)) * i;
            if (penDown) {
                Point from =new Point((int) posX, (int) posY);
                Point to = new Point((int) newPosX, (int) newPosY);
                canvas.replaceLastLine(new TurtleCanvas.Line(from, to, penColor,penWidth));
            }
            posX = newPosX;
            posY = newPosY;
            if (penDown || turtleVisible) {
                canvas.redrawAsync();
            }

            try {
                for (int j = 0; j < delay; j++) {
                    Thread.sleep(1);
                    canvas.getDisplay().readAndDispatch();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void turn(double angle) {
        this.angle = (this.angle + angle) % 360;
        if (turtleVisible) {
            canvas.redrawAsync();
        }
    }

    public void showTurtle(boolean show) {
        boolean oldTurtle = turtleVisible;
        turtleVisible = show;
        if (oldTurtle != turtleVisible) {
            canvas.redrawAsync();
        }
    }
    public void usePen(boolean use) {
        penDown = use;
    }

    public void setPenWidth(int width) {
        penWidth = width;
    }

    public void setPenColor(RGB color) {
        penColor = color;
    }



}
