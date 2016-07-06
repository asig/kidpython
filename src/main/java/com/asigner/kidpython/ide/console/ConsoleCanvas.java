package com.asigner.kidpython.ide.console;

import com.google.common.collect.Lists;
import org.eclipse.jface.resource.FontDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
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

import java.util.List;

public class ConsoleCanvas extends Canvas implements PaintListener, KeyListener {

    private static class Attr {
        int fg, bg;
        boolean bold, italic, underlined;

        static Attr decode(int val) {
            Attr a = new Attr();
            a.fg = (val >> 16) & 0xff;
            a.bg = (val >> 8) & 0xff;
            a.bold = (val & 0x4) > 0;
            a.italic = (val & 0x2) > 0;
            a.underlined = (val & 0x1) > 0;
            return a;
        }

        int encode() {
            return (fg & 0xff) << 16 | (bg & 0xff) << 8 | (bold ? 0x4 : 0) | (italic ? 0x2 : 0) | (underlined ? 0x1 : 0);
        }
    }

    private static final int LINES = 1000;

    private static final int BLACK = 0;
    private static final int WHITE = 7;
    private final Color[] colors = new Color[8];

    private final Font font;
    private final Font fontBold;
    private final Font fontItalic;
    private final Font fontBoldItalic;
    private final FontMetrics fontMetrics;

    private Font selectedFont;

    private final String[] lines = new String[LINES];
    private final List<Integer>[] attrs = new List[LINES];
    private int first;
    private int len;

    boolean inEscapeSequence = false;
    private String escapeBuffer;

    private Attr curAttr;
    private int curAttrCode;

    private boolean showCursor;
    private int cursorX, cursorY;
    private boolean cursorOn;
    private Thread cursorBlinker;

    public ConsoleCanvas(Composite parent, int style) {
        super(parent, style);

        addPaintListener(this);
        addKeyListener(this);

        for (int i = 0; i < LINES; i++) {
            lines[i] = "";
            attrs[i] = Lists.newArrayList();
        }

        first = 0;
        len = 1;
        lines[0] = "";
        cursorX = cursorY = 0;
        cursorOn = true;
        showCursor = true;

        Display disp = parent.getDisplay();

        font = new Font(disp, "Mono", 10, SWT.NONE);
        fontBold = FontDescriptor.createFrom(font).setStyle(SWT.BOLD).createFont(disp);
        fontItalic = FontDescriptor.createFrom(font).setStyle(SWT.BOLD).createFont(disp);
        fontBoldItalic = FontDescriptor.createFrom(font).setStyle(SWT.BOLD | SWT.ITALIC).createFont(disp);
        selectedFont = font;

        GC gc = new GC(this);
        gc.setFont(font);
        fontMetrics = gc.getFontMetrics();
        gc.dispose();

        colors[0] = disp.getSystemColor(SWT.COLOR_BLACK);
        colors[1] = disp.getSystemColor(SWT.COLOR_RED);
        colors[2] = disp.getSystemColor(SWT.COLOR_GREEN);
        colors[3] = disp.getSystemColor(SWT.COLOR_YELLOW);
        colors[4] = disp.getSystemColor(SWT.COLOR_BLUE);
        colors[5] = disp.getSystemColor(SWT.COLOR_MAGENTA);
        colors[6] = disp.getSystemColor(SWT.COLOR_CYAN);
        colors[7] = disp.getSystemColor(SWT.COLOR_WHITE);

        curAttr = new Attr();
        curAttr.fg = BLACK;
        curAttr.bg = WHITE;
        curAttrCode = curAttr.encode();

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

    public void showCursor(boolean showCursor) {
        this.showCursor = showCursor;
    }

    void addNoRepaint(char c) {
        if (inEscapeSequence) {
            escapeBuffer += c;
            if (c == 'm') {
                parseEscapeBuf();
                inEscapeSequence = false;
            }
        } else {
            if (c == 0x1b) {
                inEscapeSequence = true;
                escapeBuffer = "" + c;
            } else {
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
                    attrs[(first + len - 1) % LINES] = Lists.newArrayList();
                } else {
                    lines[(first + len - 1) % LINES] = lines[(first + len - 1) % LINES] + c;
                    attrs[(first + len - 1) % LINES].add(curAttrCode);
                    cursorX++;
                }
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {
        addNoRepaint(keyEvent.character);
        System.err.println(String.format("%d",(int)keyEvent.character));
        int x = cursorX * fontMetrics.getAverageCharWidth();
        int y = cursorY * fontMetrics.getHeight();
        getDisplay().syncExec(() -> redraw());
    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {
    }

    @Override
    public void paintControl(PaintEvent e) {
//        System.out.println(String.format("PaintEvent: x=%d, y=%d, w=%d, h=%d", e.x, e.y, e.width, e.height));

        GC gc = e.gc;
        Rectangle rect = this.getClientArea();
        gc.setBackground(colors[WHITE]);
        gc.fillRectangle(rect);

        gc.setForeground(e.display.getSystemColor(SWT.COLOR_BLACK));
        int lineHeight = fontMetrics.getHeight();
        gc.setFont(selectedFont);


        int firstLine = e.y/lineHeight;
        int lastLine = (e.y + e.height + lineHeight - 1)/lineHeight;
//        System.out.println(String.format("PaintEvent: drawing text from %d to %d", firstLine, lastLine));
        int y = firstLine * lineHeight;
        for (int i = firstLine; i <= lastLine && i < len; i++) {
            drawLine(gc, lines[(first + i) % LINES], attrs[(first + i) % LINES], y);
            y += lineHeight;
        }
        drawCursor(gc);
    }

    private void drawLine(GC gc, String line, List<Integer> attrs, int y) {
        int x = 0;
        int start = 0;
        while (start < line.length()) {
            // find span of same attributes.
            int attr = attrs.get(start);
            int end = start + 1;
            while (end < attrs.size() && attrs.get(end) == attr) {
                end++;
            }
            // Set the GC properties according to the attribute
            setAttr(gc, Attr.decode(attr));
            // Draw the string
            String s = line.substring(start, end);
            Point extent = gc.textExtent(s);
            gc.fillRectangle(x, y, extent.x, extent.y);
            gc.drawText(s, x, y);
            x += extent.x;

            start = end;
        }
    }

    private void setAttr(GC gc, Attr attr) {
        gc.setForeground(colors[attr.fg]);
        gc.setBackground(colors[attr.bg]);

        if (attr.bold && attr.italic) {
            gc.setFont(fontBoldItalic);
        } else if (attr.bold) {
            gc.setFont(fontBold);
        } else if (attr.italic) {
            gc.setFont(fontItalic);
        } else {
            gc.setFont(font);
        }
    }

    private void parseEscapeBuf() {
        if (escapeBuffer.charAt(1) != '[') {
            return;
        }
        switch(escapeBuffer.charAt(escapeBuffer.length() - 1)) {
            case 'm':
                // SGR – Select Graphic Rendition
                int code = Integer.parseInt(escapeBuffer.substring(2, escapeBuffer.length() - 1));
                switch (code) {
                    case 0:
                        curAttr.fg = BLACK;
                        curAttr.bg = WHITE;
                        curAttr.bold = curAttr.italic = curAttr.underlined = false;
                        break;
                    case 1:
                        curAttr.bold = true;
                        break;
                    case 3:
                        curAttr.italic = true;
                        break;
                    case 30:
                    case 31:
                    case 32:
                    case 33:
                    case 34:
                    case 35:
                    case 36:
                    case 37:
                        curAttr.fg = code - 30;
                        break;
                    case 40:
                    case 41:
                    case 42:
                    case 43:
                    case 44:
                    case 45:
                    case 46:
                    case 47:
                        curAttr.bg = code - 40;
                        break;
                }
                curAttrCode = curAttr.encode();
                break;
        }
    }

    private void drawCursor(GC gc) {
        int x = cursorX * fontMetrics.getAverageCharWidth();
        int y = cursorY * fontMetrics.getHeight();
        String line = lines[(first + cursorY) % LINES];
        String c = cursorX < line.length() ? "" + line.charAt(cursorX) : " " ;
        Color c1 = getDisplay().getSystemColor(SWT.COLOR_WHITE);
        Color c2 = getDisplay().getSystemColor(SWT.COLOR_BLACK);
        gc.setFont(font);
        if (cursorOn && showCursor) {
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
