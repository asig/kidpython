package com.asigner.kidpython.ide.controls;

import com.asigner.kidpython.ide.controls.turtle.Turtle;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.jfree.swt.SWTGraphics2D;

import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;

public class ConsoleCanvas extends Canvas {

    private final int W = 6000;
    private final int H = 8000;

    public ConsoleCanvas(Composite parent, int style) {
        super(parent, style);
        // TODO Auto-generated constructor stub

        addPaintListener(new PaintListener() {
            public void paintControl(PaintEvent e) {
                draw(e);
            }
        });
    }

    private void draw(PaintEvent e) {
        System.out.println(String.format("PaintEvent: x=%d, y=%d, w=%d, h=%d", e.x, e.y, e.width, e.height));

        GC gc = e.gc;
        Rectangle rect = this.getClientArea();
        gc.setBackground(e.display.getSystemColor(SWT.COLOR_WHITE));
        gc.fillRectangle(rect);
        gc.setBackground(e.display.getSystemColor(SWT.COLOR_RED));
        gc.drawLine(rect.x, rect.y, rect.x + rect.width, rect.y + rect.height);
        gc.drawLine(rect.x + rect.width, rect.y, rect.x, rect.y + rect.height);
    }


    @Override
    public Point computeSize(int wHint, int hHint, boolean changed) {
        return new Point(W, H);
    }

    @Override
    protected void checkSubclass() {
    }
}
