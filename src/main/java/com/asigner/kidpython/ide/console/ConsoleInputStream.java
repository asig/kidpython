package com.asigner.kidpython.ide.console;

import java.io.IOException;
import java.io.InputStream;

public class ConsoleInputStream extends InputStream {

    private final ConsoleComposite console;

    public ConsoleInputStream(ConsoleComposite console) {
        this.console = console;
    }

    @Override
    public int read() throws IOException {
        try {
            return console.getInputBuffer().read();
        } catch (InterruptedException e) {
            throw new IOException(e);
        }
    }

    @Override
    public int available() throws IOException {
        return console.getInputBuffer().getAvailable();
    }
}
