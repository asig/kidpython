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
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

import java.util.List;
import java.util.Set;

public class CodeEditor extends Composite {

    private final CodeEditorStyledText editor;
    private final StatusLine statusLine;

    public static class State {
        private int caretOfs = 0;
        private String text = "";
        private Point selection = new Point(0,0);
        private Multimap<Integer, Error> errors = ArrayListMultimap.create();

        public State(String text) {
            this.text = text;
        }
    }

    public CodeEditor(Composite parent, int style) {
        super(parent, SWT.NONE);
        GridLayout gridLayout = new GridLayout(1, false);
        gridLayout.verticalSpacing = 0;
        gridLayout.marginWidth = 0;
        gridLayout.marginHeight = 0;
        gridLayout.horizontalSpacing = 0;
        setLayout(gridLayout);

        editor = new CodeEditorStyledText(this, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
        editor.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

        editor.addCaretListener(caretEvent -> updateStatusLine());

        statusLine = new StatusLine(this, SWT.NONE);
        statusLine.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
        updateStatusLine();
    }

    public State saveState() {
        State s = new State(editor.getText());
        s.caretOfs = editor.getCaretOffset();
        s.selection = editor.getSelection();
        s.errors = editor.getLineStyler().getErrors();
        return s;
    }

    public void restoreState(State s) {
        if (s == null) {
            return;
        }
        editor.setText(s.text);
        editor.getLineStyler().parseMultiLineComments(s.text);
        editor.getLineStyler().setErrors(s.errors);
        editor.setSelection(s.selection);
        editor.setCaretOffset(s.caretOfs);
        updateStatusLine();
        editor.redraw();
    }

    private void updateStatusLine() {
        int ofs = editor.getCaretOffset();
        int line = editor.getLineAtOffset(ofs);
        int col = ofs - editor.getOffsetAtLine(line);
        statusLine.setPosition(line + 1, col + 1);
    }

    public void addModifyListener(ModifyListener listener) {
        editor.addModifyListener(listener);
    }

    public String getText() {
        return editor.getText();
    }

    public void setWellKnownWords(Set<String> wellKnown) {
        editor.setWellKnownWords(wellKnown);
    }

    public void setStylesheet(Stylesheet stylesheet) {
        editor.setStylesheet(stylesheet);
        statusLine.setStylesheet(stylesheet);
    }

    public void setActiveLine(int line) {
        editor.setActiveLine(line);
    }

    public void setErrors(List<Error> errors) {
        editor.setErrors(errors);
    }

    @Override
    protected void checkSubclass() {
        // Disable the check that prevents subclassing of SWT components
    }

}
