// Copyright 2016 Andreas Signer. All rights reserved.

package com.asigner.kidpython.ide.editor;

import com.asigner.kidpython.compiler.Error;
import com.asigner.kidpython.ide.util.SWTResources;
import com.asigner.kidpython.ide.util.SWTUtils;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.LineBackgroundEvent;
import org.eclipse.swt.custom.LineBackgroundListener;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Composite;

import java.util.List;
import java.util.Set;

public class CodeEditorStyledText extends StyledText {


    private final CodeLineStyler lineStyler;
    private Font font;
    private int lastLineCount = 0; // Used to trigger redraws for line numbering
    private int activeLine = -1;

    public CodeEditorStyledText(Composite parent, int style) {
        super(parent, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);

        Stylesheet stylesheet = Stylesheet.ALL.get(0);
        lineStyler = new CodeLineStyler(this, stylesheet);

        applyStylesheetColors(stylesheet);

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
    }

    public void setWellKnownWords(Set<String> wellKnown) {
        lineStyler.setWellKnownWords(wellKnown);
        this.getDisplay().asyncExec(this::redraw);
    }

    public void setStylesheet(Stylesheet stylesheet) {
        lineStyler.setStylesheet(stylesheet);
        applyStylesheetColors(stylesheet);
        this.getDisplay().asyncExec(this::redraw);
    }

    private void applyStylesheetColors(Stylesheet stylesheet) {
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
