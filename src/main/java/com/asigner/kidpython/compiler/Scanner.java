package com.asigner.kidpython.compiler;

import com.google.common.annotations.VisibleForTesting;

public class Scanner {

    private final String text;

    private int curPos;
    private int line;
    private int col;

    public Scanner(String text) {
        this.text = text;
        this.curPos = 0;
        this.line = 1;
        this.col = 0;
    }

    @VisibleForTesting
    char getch() {
        if (curPos >= text.length()) {
            curPos++;
            return 0;
        }
        boolean firstOnLine = curPos > 0 && text.charAt(curPos - 1) == '\n';
        char c = text.charAt(curPos++);
        if (firstOnLine) {
            line++;
            col = 1;
        } else {
            col++;
        }
        return c;
    }

    @VisibleForTesting
    void ungetch() {
        curPos--;
        if (curPos >0 && curPos <= text.length() && text.charAt(curPos) == '\n') {
            line--;
            int begin = curPos - 1;
            while (begin >= 0 && text.charAt(begin) != '\n') begin--;
            col = curPos - begin;
        } else if (curPos <= text.length()) {
            col--;
        }
    }

    public int getCol() {
        return col;
    }

    public int getLine() {
        return line;
    }
}
