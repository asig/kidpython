// Copyright 2016 Andreas Signer. All rights reserved.

package com.asigner.kidpython.ide.editor;

import com.asigner.kidpython.ide.util.SWTResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.LineBackgroundEvent;
import org.eclipse.swt.custom.LineBackgroundListener;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Composite;

import java.util.Set;

public class CodeEditor extends StyledText {

    public static class State {
        private int caretOfs = 0;
        private String text = "";
        private Point selection = new Point(0,0);

        public State(String text) {
            this.text = text;
        }
    }

    private final CodeLineStyler lineStyler;
    private Font font;
    private int lastLineCount = 0; // Used to trigger redraws for line numbering
    private int activeLine =  -1;

    public CodeEditor(Composite parent, int style) {
        super(parent, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);

        Stylesheet stylesheet = Stylesheet.ALL[0];
        lineStyler = new CodeLineStyler(this, stylesheet);

        this.setBackground(stylesheet.getDefaultBackground());

        font = new Font(parent.getDisplay(), "Roboto Mono", 10, SWT.NONE);
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
        this.getDisplay().syncExec(this::redraw);
    }

    public void setStylesheet(Stylesheet stylesheet) {
        lineStyler.setStylesheet(stylesheet);
        this.setBackground(stylesheet.getDefaultBackground());
        this.getDisplay().syncExec(this::redraw);
    }

    public void setActiveLine(int line) {
        if (line != activeLine) {
            activeLine = line;
            this.getDisplay().syncExec(this::redraw);
        }
    }

    public State saveState() {
        State s = new State(getText());
        s.caretOfs = getCaretOffset();
        s.selection = getSelection();
        return s;
    }

    public void restoreState(State s) {
        if (s == null) {
            return;
        }
        setText(s.text);
        lineStyler.parseMultiLineComments(s.text);
        setSelection(s.selection);
        setCaretOffset(s.caretOfs);
        redraw();
    }

    @Override
    protected void checkSubclass() {
    }
}
