// Copyright 2016 Andreas Signer. All rights reserved.

package com.asigner.kidpython.ide.console;

import com.asigner.kidpython.util.ByteBuffer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ConsoleComposite extends ScrolledComposite {

    private class ConsoleInputStream extends InputStream {

        @Override
        public int read() throws IOException {
            try {
                return getInputBuffer().read();
            } catch (InterruptedException e) {
                throw new IOException(e);
            }
        }

        @Override
        public int available() throws IOException {
            return getInputBuffer().getAvailable();
        }
    }

    private class ConsoleOutputStream extends OutputStream {
        @Override
        public void write(int b) throws IOException {
            ConsoleComposite.this.write((char)b);
        }

        @Override
        public void write(byte b[], int off, int len) throws IOException {
            ConsoleComposite.this.write(new String(b, off, len, "UTF-8"));
        }
    }

    private final ConsoleCanvas consoleCanvas;
    private final ConsoleInputStream consoleInputStream;
    private final ConsoleOutputStream consoleOutputStream;

    public ConsoleComposite(Composite parent, int style) {
        super(parent, style);

        setExpandHorizontal(true);
        setExpandVertical(true);

        consoleCanvas = new ConsoleCanvas(this, SWT.NONE);
        consoleCanvas.setTextModifiedListener(() -> {
            this.getDisplay().syncExec(() -> {
                setMinSize(consoleCanvas.computeSize(-1, -1));
                setOrigin(new Point(0, Integer.MAX_VALUE));
            });
        });
        setContent(consoleCanvas);
        this.consoleInputStream = new ConsoleInputStream();
        this.consoleOutputStream = new ConsoleOutputStream();
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

    public InputStream getInputStream() {
        return consoleInputStream;
    }

    public OutputStream getOutputStream() {
        return consoleOutputStream;
    }

    ByteBuffer getInputBuffer() {
        return consoleCanvas.getInputBuffer();
    }

    @Override
    public boolean forceFocus() {
        return consoleCanvas.forceFocus();
    }

    @Override
    public boolean setFocus() {
        return consoleCanvas.forceFocus();
    }
}
