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
        boolean firstOnLine = curPos > 0 && curPos <= text.length() && text.charAt(curPos - 1) == '\n';

        char c = curPos >= text.length() ? 0 : text.charAt(curPos);
        curPos++;

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
        if (curPos > 0 && curPos <= text.length() && text.charAt(curPos - 1) == '\n') {
            line--;
            int begin = curPos - 2;
            while (begin >= 0 && text.charAt(begin) != '\n') begin--;
            col = (curPos - 1) - begin;
        } else {
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
