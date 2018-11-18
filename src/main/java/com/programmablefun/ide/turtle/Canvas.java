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

package com.programmablefun.ide.turtle;

import com.google.common.collect.Lists;
import org.eclipse.jface.action.Action;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.graphics.Transform;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.jfree.swt.SWTGraphics2D;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Canvas extends org.eclipse.swt.widgets.Canvas implements MouseListener, MouseMoveListener {

    private static final int BTN_SIZE = 30;
    private static final int BTN_BORDER = 10;

    private static final float ZOOM_MIN = 0.2f;
    private static final float ZOOM_MAX = 3.0f;

    private static final String ELUSIVE_REMOVE_CIRCLE = "\uf1db";
    private static final String ELUSIVE_DOWNLOAD = "\uf146";
    private static final String ELUSIVE_DOWNLOAD_ALT = "\uf145";
    private static final String ELUSIVE_ZOOM_IN = "\uf22f";
    private static final String ELUSIVE_ZOOM_OUT = "\uf230";
    private static final String ELUSIVE_SCREENSHOT = "\uf1ec";

    public static interface Shape {
        void draw(Graphics2D g2d);
    }

    public static class Line implements Shape {
        private final Color color;
        private final BasicStroke stroke;
        private final Point start;
        private final Point end;

        public Line(Point start, Point end, RGB color, int width) {
            this.start = start;
            this.end = end;
            this.color = new Color(color.red, color.green, color.blue);
            this.stroke = new BasicStroke(width, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER);
        }

        public void draw(Graphics2D g2d) {
            g2d.setColor(color);
            g2d.setStroke(stroke);
            g2d.drawLine(start.x, start.y, end.x, end.y);
        }
    }

    public static class FilledRect implements Shape {
        private final Color color;

        private final Point p;
        private final int w;
        private final int h;

        public FilledRect(Point p, int w, int h, RGB color) {
            this.p = p;
            this.w = w;
            this.h = h;
            this.color = new Color(color.red, color.green, color.blue);
        }

        public void draw(Graphics2D g2d) {
            g2d.setColor(color);
            g2d.setColor(color);
            g2d.fillRect(p.x, p.y, w, h);
        }
    }

    private final List<Shape> shapes = Lists.newArrayList();

    private float zoom;
    private int offsetX;
    private int offsetY;

    private boolean turtleVisible;
    private double turtleX;
    private double turtleY;
    private double turtleAngle;

    private boolean lmbDown = false;
    private int lastMouseX;
    private int lastMouseY;

    private Lock shapesLock = new ReentrantLock();

    private final Font elusiveFont;

    private List<Button> buttons = Lists.newArrayList();

    public Canvas(Composite parent, int style) {
        super(parent, style | SWT.NO_BACKGROUND);

        elusiveFont = new Font(this.getDisplay(), "elusiveicons", BTN_SIZE/3, SWT.NORMAL);

        addButton(ELUSIVE_REMOVE_CIRCLE, new ResetAction(this));
        addButton(ELUSIVE_DOWNLOAD_ALT, new DownloadAction(this));
        addButton(ELUSIVE_ZOOM_IN, new ZoomInAction(this));
        addButton(ELUSIVE_ZOOM_OUT, new ZoomOutAction(this));
        addButton(ELUSIVE_SCREENSHOT, new CenterAction(this));

        reset();

        addPaintListener(this::draw);
        addListener (SWT.Resize, this::onResize);
        addMouseWheelListener(mouseEvent -> {
            if (mouseEvent.count > 0) {
                zoomIn();
            } else {
                zoomOut();
            }
        });
        addMouseListener(this);
        addMouseMoveListener(this);
    }

    public boolean isTurtleVisible() {
        return turtleVisible;
    }

    public void setTurtleVisible(boolean turtleVisible) {
        this.turtleVisible = turtleVisible;
    }

    public void setTurtlePos(double turtleX, double turtleY) {
        this.turtleX = turtleX;
        this.turtleY = turtleY;
    }

    public void setTurtleAngle(double turtleAngle) {
        this.turtleAngle = turtleAngle;
    }

    @Override
    public void mouseDoubleClick(MouseEvent mouseEvent) {
    }

    @Override
    public void mouseDown(MouseEvent mouseEvent) {
        if (mouseEvent.button == 1) {
            lmbDown = true;
            lastMouseX = mouseEvent.x;
            lastMouseY = mouseEvent.y;
        }
    }

    @Override
    public void mouseUp(MouseEvent mouseEvent) {
        if (mouseEvent.button == 1) {
            lmbDown = false;
        }
    }

    @Override
    public void mouseMove(MouseEvent mouseEvent) {
        if (lmbDown) {
            int dx = lastMouseX - mouseEvent.x;
            int dy = lastMouseY - mouseEvent.y;
            lastMouseX = mouseEvent.x;
            lastMouseY = mouseEvent.y;
            offsetX += dx;
            offsetY += dy;
            getDisplay().asyncExec(this::redraw);
        }
    }

    private void onResize(Event e) {
        Rectangle rect = getClientArea ();
        int x = rect.x + rect.width - buttons.size()*BTN_SIZE - BTN_BORDER;
        for (Button b : buttons) {
            b.setLocation(x, rect.y + BTN_BORDER);
            x += BTN_SIZE;
        }
    }

    public void reset() {
        try {
            shapesLock.lock();
            shapes.clear();
        } finally {
            shapesLock.unlock();
        }
        zoom = 1;
        offsetX = offsetY = 0;
        getDisplay().asyncExec(this::redraw);
    }

    public void clear() {
        try {
            shapesLock.lock();
            shapes.clear();
        } finally {
            shapesLock.unlock();
        }
        getDisplay().asyncExec(this::redraw);
    }

    public void setOffset(int ofsX, int ofsY) {
        offsetX = ofsX;
        offsetY = ofsY;
        this.getDisplay().asyncExec(this::redraw);
    }

    public void zoomIn() {
        if (zoom < ZOOM_MAX) {
            zoom = zoom + .1f;
            this.getDisplay().asyncExec(this::redraw);
        }
    }

    public void zoomOut() {
        if (zoom > ZOOM_MIN) {
            zoom = zoom - .1f;
            this.getDisplay().asyncExec(this::redraw);
        }
    }

    private void addButton(String label, Action action) {
        Button button = new Button(this, SWT.PUSH | SWT.FLAT | SWT.CENTER);
        button.setFont(elusiveFont);
        button.setText(label);
        button.setSize(BTN_SIZE, BTN_SIZE);
        button.setBackground(this.getDisplay().getSystemColor(SWT.COLOR_TRANSPARENT));
        button.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                action.run();
            }
        });
        buttons.add(button);
    }


    public void addShape(Shape s) {
        try {
            shapesLock.lock();
            shapes.add(s);
        } finally {
            shapesLock.unlock();
        }
    }

    public void replaceLastShape(Shape s) {
        try {
            shapesLock.lock();
            shapes.set(shapes.size() - 1, s);
        } finally {
            shapesLock.unlock();
        }
    }

    public void redrawAsync() {
        this.getDisplay().asyncExec(this::redraw);
    }

    public void redrawTurtleAsync() {
        if (turtleVisible) {
            redrawAsync();
        }
    }

    private void draw(PaintEvent e) {
        // Create the image to fill the canvas
        Image image = new Image(this.getDisplay(), this.getBounds());

        // Set up the offscreen gc
        GC gcImage = new GC(image);

        // Clear the background
        gcImage.setBackground(this.getDisplay().getSystemColor(SWT.COLOR_WHITE));
        gcImage.fillRectangle(image.getBounds());

        drawContent(gcImage);

        // Draw the offscreen buffer to the screen
        e.gc.drawImage(image, 0, 0);

        // Clean up
        image.dispose();
        gcImage.dispose();
    }

    private void drawContent(GC gc) {
        Rectangle rect = this.getClientArea();

        Transform t = new Transform(gc.getDevice());
        t.translate((rect.x + rect.width)/2.0f - offsetX, (rect.y + rect.height)/2.0f - offsetY);
        t.scale(zoom, zoom);
        gc.setTransform(t);

        SWTGraphics2D g2d = new SWTGraphics2D(gc);
        drawUntransformed(g2d);
    }

    public void drawUntransformed(Graphics2D g2d) {
        try {
            shapesLock.lock();
            for (Shape s : shapes) {
                s.draw(g2d);
            }
        } finally {
            shapesLock.unlock();
        }
        drawTurtle(g2d);
    }

    private void drawTurtle(Graphics2D g2d) {
        if (!turtleVisible) {
            return;
        }
        AffineTransform orig = g2d.getTransform();
        AffineTransform t = new AffineTransform();
        t.translate(turtleX, turtleY);
        t.scale(.25,.25);
        // HACK: SWT (at least under Linux and macOs) seems to have issues drawing paths if the rotation is a multiple
        // of 45 degrees... Therefore, let's adjust it a little.
        double a = turtleAngle;
        if (a % 45.0 < 0.01) {
            a += .01;
        }
        t.rotate(Math.toRadians(a));
        g2d.transform(t);
        TurtleShape.paint(g2d);
        g2d.setTransform(orig);
    }


    @Override
    protected void checkSubclass() {
    }
}
