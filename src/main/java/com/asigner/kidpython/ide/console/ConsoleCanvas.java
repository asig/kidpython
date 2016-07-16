package com.asigner.kidpython.ide.console;

import com.asigner.kidpython.util.ByteBuffer;
import com.google.common.collect.Lists;
import org.eclipse.jface.resource.FontDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontMetrics;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static com.asigner.kidpython.ide.util.AnsiEscapeCodes.BG_BLACK;
import static com.asigner.kidpython.ide.util.AnsiEscapeCodes.BG_BLUE;
import static com.asigner.kidpython.ide.util.AnsiEscapeCodes.BG_CYAN;
import static com.asigner.kidpython.ide.util.AnsiEscapeCodes.BG_GREEN;
import static com.asigner.kidpython.ide.util.AnsiEscapeCodes.BG_MAGENTA;
import static com.asigner.kidpython.ide.util.AnsiEscapeCodes.BG_RED;
import static com.asigner.kidpython.ide.util.AnsiEscapeCodes.BG_WHITE;
import static com.asigner.kidpython.ide.util.AnsiEscapeCodes.BG_YELLOW;
import static com.asigner.kidpython.ide.util.AnsiEscapeCodes.BOLD;
import static com.asigner.kidpython.ide.util.AnsiEscapeCodes.FG_BLACK;
import static com.asigner.kidpython.ide.util.AnsiEscapeCodes.FG_BLUE;
import static com.asigner.kidpython.ide.util.AnsiEscapeCodes.FG_CYAN;
import static com.asigner.kidpython.ide.util.AnsiEscapeCodes.FG_GREEN;
import static com.asigner.kidpython.ide.util.AnsiEscapeCodes.FG_MAGENTA;
import static com.asigner.kidpython.ide.util.AnsiEscapeCodes.FG_RED;
import static com.asigner.kidpython.ide.util.AnsiEscapeCodes.FG_WHITE;
import static com.asigner.kidpython.ide.util.AnsiEscapeCodes.FG_YELLOW;
import static com.asigner.kidpython.ide.util.AnsiEscapeCodes.IMAGE_NEGATIVE;
import static com.asigner.kidpython.ide.util.AnsiEscapeCodes.IMAGE_POSITIVE;
import static com.asigner.kidpython.ide.util.AnsiEscapeCodes.ITALIC;
import static com.asigner.kidpython.ide.util.AnsiEscapeCodes.RESET;

public class ConsoleCanvas extends Canvas implements PaintListener, KeyListener {

    private static class Attr {
        int fg, bg;
        boolean inverse;
        boolean bold, italic, underlined;

        static Attr decode(int val) {
            Attr a = new Attr();
            a.fg = (val >> 16) & 0xff;
            a.bg = (val >> 8) & 0xff;
            a.inverse = (val & 0x8) > 0;
            a.bold = (val & 0x4) > 0;
            a.italic = (val & 0x2) > 0;
            a.underlined = (val & 0x1) > 0;
            return a;
        }

        int encode() {
            return (fg & 0xff) << 16 | (bg & 0xff) << 8 | (inverse ? 0x8 : 0) | (bold ? 0x4 : 0) | (italic ? 0x2 : 0) | (underlined ? 0x1 : 0);
        }
    }

    private static final int LINES = 1000;

    private static final int BLACK = 0;
    private static final int WHITE = 7;
    private final Color[] COLORS = new Color[8];

    private final Lock textLock = new ReentrantLock();

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

    private boolean inEscapeSequence = false;
    private String escapeBuffer;

    private Attr curAttr;
    private int curAttrCode;

    private boolean showCursor;
    private int cursorX, cursorY;
    private boolean cursorOn;

    private ByteBuffer inputBuffer = new ByteBuffer(128 * 1024);

    private Runnable textModifiedListener = () -> {};

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
        cursorX = cursorY = 0;
        cursorOn = true;
        showCursor = true;

        Display disp = parent.getDisplay();

        this.setMenu(createContextMenu());

        font = new Font(parent.getDisplay(), "Roboto Mono", 10, SWT.NONE);
        fontBold = FontDescriptor.createFrom(font).setStyle(SWT.BOLD).createFont(disp);
        fontItalic = FontDescriptor.createFrom(font).setStyle(SWT.BOLD).createFont(disp);
        fontBoldItalic = FontDescriptor.createFrom(font).setStyle(SWT.BOLD | SWT.ITALIC).createFont(disp);
        selectedFont = font;

        GC gc = new GC(this);
        gc.setFont(font);
        fontMetrics = gc.getFontMetrics();
        gc.dispose();

        COLORS[0] = disp.getSystemColor(SWT.COLOR_BLACK);
        COLORS[1] = disp.getSystemColor(SWT.COLOR_RED);
        COLORS[2] = disp.getSystemColor(SWT.COLOR_GREEN);
        COLORS[3] = disp.getSystemColor(SWT.COLOR_YELLOW);
        COLORS[4] = disp.getSystemColor(SWT.COLOR_BLUE);
        COLORS[5] = disp.getSystemColor(SWT.COLOR_MAGENTA);
        COLORS[6] = disp.getSystemColor(SWT.COLOR_CYAN);
        COLORS[7] = disp.getSystemColor(SWT.COLOR_WHITE);

        curAttr = new Attr();
        curAttr.fg = BLACK;
        curAttr.bg = WHITE;
        curAttrCode = curAttr.encode();

        Thread cursorBlinker = new Thread() {
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

    private Menu createContextMenu() {
        Menu menu = new Menu(this);
        MenuItem clearItem = new MenuItem(menu, SWT.NONE);
        clearItem.setText("Clear");
        clearItem.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                clear();
            }
        });
        return menu;
    }

    public void setTextModifiedListener(Runnable textModifiedListener) {
        this.textModifiedListener = textModifiedListener;
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

    public void write(String s) {
        textLock.lock();
        try {
            for (char c : s.toCharArray()) {
                writeNoRepaint(c);
            }
            requestRedraw();
        } finally {
            textLock.unlock();
        }
        textModifiedListener.run();
    }

    public void write(char c) {
        textLock.lock();
        try {
            writeNoRepaint(c);
            requestRedraw();
        } finally {
            textLock.unlock();
        }
        textModifiedListener.run();
    }

    public void clear() {
        textLock.lock();
        try {
            this.first = 0;
            this.len = 1;
            this.lines[0] = "";
            this.attrs[0] = Lists.newArrayList();
            cursorX = cursorY = 0;
            requestRedraw();
        } finally {
            textLock.unlock();
        }
        textModifiedListener.run();
    }

    private void requestRedraw() {
        this.getDisplay().asyncExec(this::redraw);
    }

    private void writeNoRepaint(char c) {
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

    ByteBuffer getInputBuffer() {
        return inputBuffer;
    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {
        char c = keyEvent.character;
        if (c == '\0') {
            return;
        }

        try {
            inputBuffer.write((byte)c);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (c == '\r') {
            c = '\n';
        }
        if (c != '\n' && Character.isISOControl(c)) {
            return;
        }
        writeNoRepaint(c);
        textModifiedListener.run();

        // redraw the whole line
        int h = fontMetrics.getHeight();
        int y = cursorY * h;
        if (c == '\n') {
            // potentially, we scrolled. repaint everything
            y = 0;
            h = getClientArea().height;
        }
        int finalY = y;
        int finalH = h;
        getDisplay().syncExec(() -> redraw(0, finalY, Integer.MAX_VALUE, finalH, false));
    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {
    }

    @Override
    public void paintControl(PaintEvent e) {
//        System.out.println(String.format("PaintEvent: x=%d, y=%d, w=%d, h=%d", e.x, e.y, e.width, e.height));

        GC gc = e.gc;
        Rectangle rect = this.getClientArea();
        gc.setBackground(COLORS[WHITE]);
        gc.fillRectangle(rect);

        gc.setForeground(e.display.getSystemColor(SWT.COLOR_BLACK));
        int lineHeight = fontMetrics.getHeight();
        gc.setFont(selectedFont);


        int firstLine = e.y/lineHeight;
        int lastLine = (e.y + e.height + lineHeight - 1)/lineHeight;
        int y = firstLine * lineHeight;
        try {
            textLock.lock();
            for (int i = firstLine; i <= lastLine && i < len; i++) {
                drawLine(gc, lines[(first + i) % LINES], attrs[(first + i) % LINES], y);
                y += lineHeight;
            }
            drawCursor(gc);
        } finally {
            textLock.unlock();
        }
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

        gc.setForeground(COLORS[attr.inverse ? attr.bg : attr.fg]);
        gc.setBackground(COLORS[attr.inverse ? attr.fg : attr.bg]);

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
        switch(escapeBuffer) {
            case RESET:
                curAttr.fg = BLACK;
                curAttr.bg = WHITE;
                curAttr.bold = curAttr.italic = curAttr.underlined = false;
                curAttr.inverse = false;
                break;
            case BOLD:
                curAttr.bold = true;
                break;
            case ITALIC:
                curAttr.italic = true;
                break;
            case IMAGE_NEGATIVE:
                curAttr.inverse = true;
                break;
            case IMAGE_POSITIVE:
                curAttr.inverse = false;
                break;
            case FG_BLACK:
                curAttr.fg = 0;
                break;
            case FG_RED:
                curAttr.fg = 1;
                break;
            case FG_GREEN:
                curAttr.fg = 2;
                break;
            case FG_YELLOW:
                curAttr.fg = 3;
                break;
            case FG_BLUE:
                curAttr.fg = 4;
                break;
            case FG_MAGENTA:
                curAttr.fg = 5;
                break;
            case FG_CYAN:
                curAttr.fg = 6;
                break;
            case FG_WHITE:
                curAttr.fg = 7;
                break;
            case BG_BLACK:
                curAttr.bg = 0;
                break;
            case BG_RED:
                curAttr.bg = 1;
                break;
            case BG_GREEN:
                curAttr.bg = 2;
                break;
            case BG_YELLOW:
                curAttr.bg = 3;
                break;
            case BG_BLUE:
                curAttr.bg = 4;
                break;
            case BG_MAGENTA:
                curAttr.bg = 5;
                break;
            case BG_CYAN:
                curAttr.bg = 6;
                break;
            case BG_WHITE:
                curAttr.bg = 7;
                break;
        }
        curAttrCode = curAttr.encode();
    }

    private void drawCursor(GC gc) {
        int x = cursorX * fontMetrics.getAverageCharWidth();
        int y = cursorY * fontMetrics.getHeight();
        String line = lines[(first + cursorY) % LINES];
        String c = cursorX < line.length() ? "" + line.charAt(cursorX) : " " ;
        Color c1 = getDisplay().getSystemColor(SWT.COLOR_WHITE);
        Color c2 = getDisplay().getSystemColor(SWT.COLOR_BLACK);
        gc.setFont(font);
        if (cursorOn && showCursor && this.isFocusControl()) {
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

    public static void main(String ... args) {
        BufferedReader r = new BufferedReader(new InputStreamReader(System.in));
        String res = null;
        try {
            res = r.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.err.println(res);
    }
}
