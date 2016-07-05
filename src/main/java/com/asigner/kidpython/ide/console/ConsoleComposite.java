// Copyright 2016 Andreas Signer. All rights reserved.

package com.asigner.kidpython.ide.console;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

public class ConsoleComposite extends ScrolledComposite {
    private final ConsoleCanvas consoleCanvas;

    public ConsoleComposite(Composite parent, int style) {
        super(parent, style);


        setExpandHorizontal(true);
        setExpandVertical(true);

        consoleCanvas = new ConsoleCanvas(this, SWT.NONE);
        setContent(consoleCanvas);
    }

    public void add(String s) {
        for (char c : s.toCharArray()) {
            consoleCanvas.addNoRepaint(c);
        }
        setMinSize(consoleCanvas.computeSize(-1, -1));
        setOrigin(new Point(0, Integer.MAX_VALUE));
        Display.getCurrent().asyncExec(this::redraw);
    }

    public void add(char c) {
        consoleCanvas.addNoRepaint(c);
        setMinSize(consoleCanvas.computeSize(-1, -1));
        setOrigin(new Point(0, Integer.MAX_VALUE));
        Display.getCurrent().asyncExec(this::redraw);
    }

    @Override
    protected void checkSubclass() {
    }
}
