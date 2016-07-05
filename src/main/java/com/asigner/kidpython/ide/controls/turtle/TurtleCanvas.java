// Copyright 2016 Andreas Signer. All rights reserved.

package com.asigner.kidpython.ide.controls.turtle;

import com.google.common.collect.Lists;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.jfree.swt.SWTGraphics2D;

import java.awt.geom.AffineTransform;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static jdk.nashorn.internal.runtime.regexp.joni.encoding.CharacterType.W;

public class TurtleCanvas extends Canvas {

    public static class Line {
        private final Color color;
        private final int width;
        private final Point start;
        private final Point end;

        Line(Point start, Point end, Color color, int width) {
            this.start = start;
            this.end = end;
            this.color = color;
            this.width = width;
        }
    }

    private final List<Line> lines;
    private double angle;
    private double posX, posY;
    private boolean penDown;
    private boolean turtleVisible;

    public TurtleCanvas(Composite parent, int style) {
        super(parent, style);

        Rectangle rect = this.getClientArea();

        this.lines = Lists.newArrayList();
        this.angle = 0;
        this.posX = (rect.x + rect.width)/2;
        this.posY = (rect.y + rect.height)/2;
        this.penDown = true;
        this.turtleVisible = true;

        addPaintListener(this::draw);
    }

    private void draw(PaintEvent e) {
        GC gc = e.gc;
        Rectangle rect = this.getClientArea();
        gc.setBackground(e.display.getSystemColor(SWT.COLOR_WHITE));
        gc.fillRectangle(rect);

        for (Line line : lines) {
            drawLine(gc, line);
        }
        drawTurtle(gc);
    }

    private void drawTurtle(GC gc) {
        if (!turtleVisible) {
            return;
        }
        SWTGraphics2D g2s = new SWTGraphics2D(gc);
        AffineTransform orig = g2s.getTransform();
        AffineTransform t = new AffineTransform();
        t.translate(posX, posY);
        t.scale(.25,.25);
        t.rotate(angle);
        g2s.transform(t);
        Turtle.paint(g2s);
        g2s.setTransform(orig);
    }

    private void drawLine(GC gc, Line line) {
        gc.setForeground(line.color);
        gc.setLineWidth(line.width);
        gc.drawLine(line.start.x, line.start.y, line.end.x, line.end.y);
    }

    @Override
    protected void checkSubclass() {
    }
}
