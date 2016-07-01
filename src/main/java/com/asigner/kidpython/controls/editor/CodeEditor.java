// Copyright 2016 Andreas Signer. All rights reserved.

package com.asigner.kidpython.controls.editor;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;

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

    public CodeEditor(Composite parent, int style) {
        super(parent, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);

        Stylesheet stylesheet = Stylesheet.MONOKAI_SUBLIME;
        lineStyler = new CodeLineStyler(stylesheet);

        this.setBackground(stylesheet.getDefaultBackground());

        font = new Font(parent.getDisplay(), "Mono", 10, SWT.NONE);
        this.addDisposeListener(new DisposeListener() {
            @Override
            public void widgetDisposed(DisposeEvent disposeEvent) {
                font.dispose();
            }
        });

        addLineStyleListener(lineStyler);
        setFont(font);
        addModifyListener(new ModifyListener() {
            @Override
            public void modifyText(ModifyEvent modifyEvent) {
                if (lineStyler.parseMultiLineComments(getText())) {
                    redraw();
                }
            }
        });

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
        setSelection(s.selection);
        setCaretOffset(s.caretOfs);
    }

    @Override
    protected void checkSubclass() {
    }
}
