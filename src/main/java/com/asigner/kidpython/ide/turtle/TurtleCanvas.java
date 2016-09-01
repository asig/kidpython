// Copyright 2016 Andreas Signer. All rights reserved.

package com.asigner.kidpython.ide.turtle;

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
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.jfree.swt.SWTGraphics2D;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class TurtleCanvas extends Canvas implements MouseListener, MouseMoveListener {

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

    public static class Line {
        private final RGB color;
        private final int width;
        private final Point start;
        private final Point end;

        Line(Point start, Point end, RGB color, int width) {
            this.start = start;
            this.end = end;
            this.color = color;
            this.width = width;
        }
    }

    private final List<Line> lines = Lists.newArrayList();
    private boolean slowMotion;
    private double angle;
    private double posX, posY;
    private boolean penDown;
    private boolean turtleVisible;
    private RGB penColor;
    private int penWidth;
    private float zoom;
    private int offsetX;
    private int offsetY;

    private boolean lmbDown = false;
    private int lastMouseX;
    private int lastMouseY;

    private Lock linesLock = new ReentrantLock();

    private final Font elusiveFont;

    private List<Button> buttons = Lists.newArrayList();

    public TurtleCanvas(Composite parent, int style) {
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

    public void setSlowMotion(boolean slowMotion) {
        this.slowMotion = slowMotion;
    }

    public void reset() {
        try {
            linesLock.lock();
            lines.clear();
        } finally {
            linesLock.unlock();
        }
        slowMotion = false;
        posX = posY = angle = 0;
        penDown = true;
        penColor = new RGB(0,0,0);
        penWidth = 1;
        turtleVisible = true;
        zoom = 1;
        offsetX = offsetY = 0;
        getDisplay().asyncExec(this::redraw);
    }

    public void download() {
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

    public void turnTo(double angle) {
        this.angle = angle;
        if (turtleVisible) {
            this.getDisplay().syncExec(this::redraw);
        }
    }

    public void moveTo(double x, double y) {
        this.posX = x;
        this.posY = y;
        if (turtleVisible) {
            this.getDisplay().syncExec(this::redraw);
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
            addLine(new Point(0,0), new Point(0,0));
        }
        double origPosX = posX;
        double origPosY = posY;
        for (double i = startLen; i <= len; i ++) {
            posX = origPosX;
            posY = origPosY;
            double newPosX = posX + Math.sin(Math.toRadians(angle)) * i;
            double newPosY = posY - Math.cos(Math.toRadians(angle)) * i;
            if (penDown) {
                replaceLastLine(new Point((int) posX, (int) posY), new Point((int) newPosX, (int) newPosY));
            }
            posX = newPosX;
            posY = newPosY;
            if (penDown || turtleVisible) {
                this.getDisplay().syncExec(this::redraw);
            }

            try {
                for (int j = 0; j < delay; j++) {
                    Thread.sleep(1);
                    this.getDisplay().readAndDispatch();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void addLine(Point from, Point to) {
        try {
            linesLock.lock();
            lines.add(new Line(from, to, penColor,penWidth));
        } finally {
            linesLock.unlock();
        }
    }

    private void replaceLastLine(Point from, Point to) {
        try {
            linesLock.lock();
            lines.set(lines.size() - 1, new Line(from, to, penColor,penWidth));
        } finally {
            linesLock.unlock();
        }
    }

    public void turn(int angle) {
        this.angle = (this.angle + angle) % 360;
        if (turtleVisible) {
            this.getDisplay().asyncExec(this::redraw);
        }
    }

    public void showTurtle(boolean show) {
        boolean oldTurtle = turtleVisible;
        turtleVisible = show;
        if (oldTurtle != turtleVisible) {
            this.getDisplay().asyncExec(this::redraw);
        }
    }

    public void usePen(boolean use) {
        penDown = use;
    }

    public void setPen(RGB color, int width) {
        penColor = color;
        penWidth = width;
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
            linesLock.lock();
            for (Line line : lines) {
                drawLine(g2d, line);
            }
        } finally {
            linesLock.unlock();
        }
        drawTurtle(g2d);
    }

    private void drawTurtle(Graphics2D g2d) {
        if (!turtleVisible) {
            return;
        }
        AffineTransform orig = g2d.getTransform();
        AffineTransform t = new AffineTransform();
        t.translate(posX, posY);
        t.scale(.25,.25);
        t.rotate(Math.toRadians(angle));
        g2d.transform(t);
        Turtle.paint(g2d);
        g2d.setTransform(orig);
    }

    private void drawLine(Graphics2D g2d, Line line) {
        g2d.setColor(new Color(line.color.red, line.color.green, line.color.blue));
        g2d.setStroke(new BasicStroke(line.width, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER));
        g2d.drawLine(line.start.x, line.start.y, line.end.x, line.end.y);
    }

    @Override
    protected void checkSubclass() {
    }
}
