package com.asigner.kidpython.ide.console;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontMetrics;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

import java.util.Timer;
import java.util.TimerTask;

public class ConsoleCanvas extends Canvas {
    private static final int LINES = 1000;

    private final Font font;
    private final FontMetrics fontMetrics;

    private final String[] lines = new String[LINES];
    private int first;
    private int len;

    private int cursorX, cursorY;
    private boolean cursorOn;
    private Thread cursorBlinker;

    public ConsoleCanvas(Composite parent, int style) {
        super(parent, style);
        // TODO Auto-generated constructor stub

        addPaintListener(new PaintListener() {
            public void paintControl(PaintEvent e) {
                draw(e);
            }
        });

        first = 0;
        len = 1;
        lines[0] = "";
        cursorX = cursorY = 0;
        cursorOn = true;

        font = new Font(parent.getDisplay(), "Mono", 10, SWT.NONE);
        GC gc = new GC(this);
        gc.setFont(font);
        fontMetrics = gc.getFontMetrics();
        gc.dispose();

        cursorBlinker = new Thread() {
            @Override
            public void run() {
                for (; ; ) {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        return;
                    }
                    flipCursor();
                }
            }
        };
        cursorBlinker.setDaemon(true);
        cursorBlinker.start();
    }

    @Override
    public void dispose() {
        super.dispose();
    }

    private void flipCursor() {
        cursorOn = !cursorOn;
        if(!isDisposed()) {
            Display display = this.getDisplay();
            if (display != null) {
                // Just repaint the cursor area
                int x = cursorX * fontMetrics.getAverageCharWidth();
                int y = cursorY * fontMetrics.getHeight();
                display.syncExec(() -> redraw(x, y, fontMetrics.getAverageCharWidth(), fontMetrics.getHeight(), false));
            }
        }
    }

    void addNoRepaint(char c) {
        if (c == '\n') {
            len++;
            cursorY++;
            cursorX = 0;
            if (len > LINES) {
                len--;
                cursorY--;
                first = (first + 1) % LINES;
            }
            lines[(first + len - 1) % LINES] = "";
        } else {
            lines[(first + len - 1) % LINES] = lines[(first + len - 1) % LINES] + c;
            cursorX++;
        }
    }

    private void draw(PaintEvent e) {
//        System.out.println(String.format("PaintEvent: x=%d, y=%d, w=%d, h=%d", e.x, e.y, e.width, e.height));

        GC gc = e.gc;
        Rectangle rect = this.getClientArea();
        gc.setBackground(e.display.getSystemColor(SWT.COLOR_WHITE));
        gc.fillRectangle(rect);

        gc.setForeground(e.display.getSystemColor(SWT.COLOR_BLACK));
        int lineHeight = fontMetrics.getHeight();
        gc.setFont(font);


        int firstLine = e.y/lineHeight;
        int lastLine = (e.y + e.height + lineHeight - 1)/lineHeight;
//        System.out.println(String.format("PaintEvent: drawing text from %d to %d", firstLine, lastLine));
        int y = firstLine * lineHeight;
        for (int i = firstLine; i <= lastLine && i < len; i++) {
            gc.drawText(lines[(first + i) % LINES], 0, y);
            y += lineHeight;
        }
        drawCursor(gc);
    }

    private void drawCursor(GC gc) {
        int x = cursorX * fontMetrics.getAverageCharWidth();
        int y = cursorY * fontMetrics.getHeight();
        String line = lines[(first + cursorY) % LINES];
        String c = cursorX < line.length() ? "" + line.charAt(cursorX) : " " ;
        Color c1 = getDisplay().getSystemColor(SWT.COLOR_WHITE);
        Color c2 = getDisplay().getSystemColor(SWT.COLOR_BLACK);
        gc.setFont(font);
        if (cursorOn) {
            gc.setForeground(c1);
            gc.setBackground(c2);
        } else {
            gc.setForeground(c2);
            gc.setBackground(c1);
        }
        gc.drawText(c, x, y);
    }

    @Override
    public Point computeSize(int wHint, int hHint, boolean changed) {
        int maxLen = 0;
        for (int i = first; i < first + len; i++) {
            maxLen = Math.max(maxLen, lines[i % LINES].length());
        }
        return new Point(maxLen * fontMetrics.getAverageCharWidth(), len * fontMetrics.getHeight());
    }

    @Override
    protected void checkSubclass() {
    }
}
