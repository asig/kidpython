// Copyright 2016 Andreas Signer. All rights reserved.

package com.asigner.kidpython.ide.console;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
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
        consoleCanvas.setTextModifiedListener(() -> {
            setMinSize(consoleCanvas.computeSize(-1, -1));
            setOrigin(new Point(0, Integer.MAX_VALUE));
        });
        setContent(consoleCanvas);
    }

    @Override
    protected void checkSubclass() {
    }

    public void write(String s) {
        consoleCanvas.write(s);
    }

    public void write(char c) {
        consoleCanvas.write(c);
    }
}
