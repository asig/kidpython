package com.asigner.kidpython.compiler;

public class Position {
    private final int line;
    private final int col;

    public Position(int line, int col) {
        this.col = col;
        this.line = line;
    }

    public int getCol() {
        return col;
    }

    public int getLine() {
        return line;
    }
}
