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

package com.programmablefun.ide.editor;

import com.programmablefun.compiler.Error;
import com.programmablefun.ide.util.SWTResources;
import com.programmablefun.ide.util.SWTUtils;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.LineBackgroundEvent;
import org.eclipse.swt.custom.LineBackgroundListener;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Composite;

import java.util.List;
import java.util.Set;

public class CodeEditorStyledText extends StyledText {

    private final CodeLineStyler lineStyler;

    private Stylesheet stylesheet;
    private Font font;
    private int lastLineCount = 0; // Used to trigger redraws for line numbering
    private int activeLine = -1;

    public CodeEditorStyledText(Composite parent, int style) {
        super(parent, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);

        stylesheet = Stylesheet.ALL.get(0);
        lineStyler = new CodeLineStyler(this, stylesheet);

        this.setBackground(stylesheet.getDefaultBackground());
        this.setSelectionBackground(stylesheet.getSelectionBackground());
        this.setSelectionForeground(stylesheet.getSelectionForeground());

        font = new Font(parent.getDisplay(), "Roboto Mono", SWTUtils.scaleFont(10), SWT.NONE);
        this.addDisposeListener(new DisposeListener() {
            @Override
            public void widgetDisposed(DisposeEvent disposeEvent) {
                font.dispose();
            }
        });
        this.addLineBackgroundListener(new LineBackgroundListener() {
            @Override
            public void lineGetBackground(LineBackgroundEvent lineBackgroundEvent) {
                int line = getLineAtOffset(lineBackgroundEvent.lineOffset) + 1; // activeLine is 1-based
                if (line == activeLine) {
                    lineBackgroundEvent.lineBackground = SWTResources.getColor(new RGB(255,0,0));
                }
            }
        });

        addLineStyleListener(lineStyler);
        setFont(font);
        addModifyListener(modifyEvent -> {
            int lineCount = getLineCount();
            if (lastLineCount != lineCount || lineStyler.parseMultiLineComments(getText())) {
                redraw();
            }
            lastLineCount = lineCount;
        });

        // For some reason, the gutter separator is not a complete line if we only draw it in drawBackground().
        // Therefore, we add an additional paint listener that *only* draws the gutter separator line.
        // Note that we must not erase the background in the paint listener, because the StyledText is already
        // painted when it gets called.
        this.addPaintListener(this::drawGutterSeparator);

    }

    private void drawGutterSeparator(PaintEvent e) {
        int gutterWidth = lineStyler.getGutterWidth();
        if (e.x < gutterWidth && gutterWidth < e.x + e.width) {
            // We need to repaint the gutter separator
            e.gc.setForeground(stylesheet.getGutterForeground());
            e.gc.drawLine(gutterWidth, e.y, gutterWidth, e.y + e.height);
        }
    }

    @Override
    public void drawBackground(GC gc, int x, int y, int width, int height) {
        super.drawBackground(gc, x, y, width, height);
        int gutterWidth = lineStyler.getGutterWidth();
        if (x < gutterWidth) {
            // We need to repaint the gutter. the separator is drawn in drawGutterSeparator()        	
            gc.setBackground(stylesheet.getGutterBackground());
            gc.fillRectangle(0, y, gutterWidth, y + height);
        }
    }

    public void setWellKnownWords(Set<String> wellKnown) {
        lineStyler.setWellKnownWords(wellKnown);
        this.getDisplay().asyncExec(this::redraw);
    }

    public void setStylesheet(Stylesheet stylesheet) {
        lineStyler.setStylesheet(stylesheet);
        applyStylesheet(stylesheet);
        this.getDisplay().asyncExec(this::redraw);
    }

    private void applyStylesheet(Stylesheet stylesheet) {
        this.stylesheet = stylesheet;
        this.setBackground(stylesheet.getDefaultBackground());
        this.setSelectionBackground(stylesheet.getSelectionBackground());
        this.setSelectionForeground(stylesheet.getSelectionForeground());
    }

    public void setActiveLine(int line) {
        if (line != activeLine) {
            activeLine = line;
            this.getDisplay().asyncExec(this::redraw);
        }
    }

    public CodeLineStyler getLineStyler() {
        return lineStyler;
    }

    public void setErrors(List<Error> errors) {
        Multimap<Integer, Error> map = ArrayListMultimap.create();
        for (Error e : errors) {
            map.put(e.getPos().getLine(), e);
        }
        lineStyler.setErrors(map);
        redraw();
    }

    @Override
    protected void checkSubclass() {
    }
}
