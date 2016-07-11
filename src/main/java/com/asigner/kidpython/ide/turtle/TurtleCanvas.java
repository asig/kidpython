// Copyright 2016 Andreas Signer. All rights reserved.

package com.asigner.kidpython.ide.turtle;

import com.asigner.kidpython.ide.util.SWTResources;
import com.google.common.collect.Lists;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.RGBA;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.graphics.Transform;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.jfree.swt.SWTGraphics2D;

import java.awt.geom.AffineTransform;
import java.util.List;

public class TurtleCanvas extends Canvas {

    private static final String ELUSIVE_REMOVE_CIRCLE = "\uf1db";
    private static final String ELUSIVE_DOWNLOAD = "\uf146";
    private static final String ELUSIVE_DOWNLOAD_ALT = "\uf145";
    private static final String ELUSIVE_ZOOM_IN = "\uf22f";
    private static final String ELUSIVE_ZOOM_OUT = "\uf230";

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
    private Color penColor;
    private int penWidth;

    private List<Button> buttons = Lists.newArrayList();

    public TurtleCanvas(Composite parent, int style) {
        super(parent, style);

        Font elusiveFont = new Font(this.getDisplay(), "elusiveicons", 10, SWT.NORMAL);


        {
            Button button = new Button(this, SWT.PUSH | SWT.FLAT | SWT.NO_TRIM);
            button.setFont(elusiveFont);
            button.setText(ELUSIVE_REMOVE_CIRCLE);
            button.setSize(30, 30);
            button.setLocation(10 + 0 * 30, 10);
            button.setBackground(this.getDisplay().getSystemColor(SWT.COLOR_TRANSPARENT));
            buttons.add(button);
        }

        {
            Button button = new Button(this, SWT.PUSH | SWT.FLAT | SWT.NO_TRIM);
            button.setFont(elusiveFont);
            button.setText(ELUSIVE_DOWNLOAD_ALT);
            button.setSize(30, 30);
            button.setLocation(10 + 1 * 30, 10);
            button.setBackground(this.getDisplay().getSystemColor(SWT.COLOR_TRANSPARENT));
            buttons.add(button);
        }

        {
            Button button = new Button(this, SWT.PUSH | SWT.FLAT | SWT.NO_TRIM);
            button.setFont(elusiveFont);
            button.setText(ELUSIVE_ZOOM_IN);
            button.setSize(30, 30);
            button.setLocation(10 + 2 * 30, 10);
            button.setBackground(this.getDisplay().getSystemColor(SWT.COLOR_TRANSPARENT));
            buttons.add(button);
        }

        {
            Button button = new Button(this, SWT.PUSH | SWT.FLAT | SWT.NO_TRIM);
            button.setFont(elusiveFont);
            button.setText(ELUSIVE_ZOOM_OUT);
            button.setSize(30, 30);
            button.setLocation(10 + 3 * 30, 10);
            button.setBackground(this.getDisplay().getSystemColor(SWT.COLOR_TRANSPARENT));
            buttons.add(button);
        }

        this.lines = Lists.newArrayList();
        this.angle = 0;
        this.posX = 0;
        this.posY = 0;
        this.penDown = true;
        this.turtleVisible = true;

        penColor = SWTResources.getColor(new RGB(0,0,0));
        penWidth = 1;

        addPaintListener(this::draw);
        addListener (SWT.Resize,  new Listener() {
            public void handleEvent (Event e) {
                Rectangle rect = getClientArea ();
                int x = rect.x + rect.width - buttons.size()*30 - 10;
                for (Button b : buttons) {
                    b.setLocation(x, rect.y + 10);
                    x += 30;
                }
            }
        });
    }


    public void move(double len) {
        if (len <= 0) {
            return;
        }
        double newPosX = posX + Math.sin(Math.toRadians(angle)) * len;
        double newPosY = posY - Math.cos(Math.toRadians(angle)) * len;
        if (penDown) {
            lines.add(new Line(
                    new Point((int)posX, (int)posY),
                    new Point((int)newPosX, (int)newPosY),
                    penColor,
                    penWidth));
        }
        posX = newPosX;
        posY = newPosY;
        if (penDown || turtleVisible) {
            Display.getCurrent().asyncExec(this::redraw);
        }
    }

    public void turn(int angle) {
        this.angle = (this.angle + angle) % 360;
    }

    public void showTurtle(boolean show) {
        boolean oldTurtle = turtleVisible;
        turtleVisible = show;
        if (oldTurtle != turtleVisible) {
            Display.getCurrent().asyncExec(this::redraw);
        }
    }

    public void usePen(boolean use) {
        penDown = use;
    }

    public void setPen(RGB color, int width) {
        setPen(new RGBA(color.red, color.green, color.blue, 255), width);
    }

    public void setPen(RGBA color, int width) {
        penColor = SWTResources.getColor(color);
        penWidth = width;
    }

    private void draw(PaintEvent e) {
        GC gc = e.gc;
        Rectangle rect = this.getClientArea();
        gc.setBackground(e.display.getSystemColor(SWT.COLOR_WHITE));
        gc.fillRectangle(rect);

        Transform t = new Transform(gc.getDevice());
        t.translate((rect.x + rect.width)/2.0f, (rect.y + rect.height)/2.0f);
        gc.setTransform(t);

        gc.setLineCap(SWT.CAP_ROUND);
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
        t.rotate(Math.toRadians(angle));
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
