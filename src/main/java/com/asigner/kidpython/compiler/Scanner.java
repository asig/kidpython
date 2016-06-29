package com.asigner.kidpython.compiler;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.ImmutableMap;

import java.util.Map;

public class Scanner {

    private static final Map<String, Token.Type> keywords = ImmutableMap.<String, Token.Type>builder()
            .put("func", Token.Type.FUNC)
            .put("for", Token.Type.FOR)
            .put("end", Token.Type.END)
            .put("if", Token.Type.IF)
            .put("then", Token.Type.THEN)
            .put("elseif", Token.Type.ELSEIF)
            .put("else", Token.Type.ELSE)
            .put("step", Token.Type.STEP)
            .put("in", Token.Type.IN)
            .put("to", Token.Type.TO)
            .put("do", Token.Type.DO)
            .put("while", Token.Type.WHILE)
            .put("repeat", Token.Type.REPEAT)
            .put("until", Token.Type.UNTIL)
            .put("return", Token.Type.RETURN)
            .put("and", Token.Type.AND)
            .put("or", Token.Type.OR)
            .build();

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

    Token next() {
        // Read over whitespace
        while (Character.isWhitespace(getch())) ;

        ungetch();

        char c = getch();
        int col = this.getCol();
        int line = this.getLine();
        if (Character.isLetter(c)) {
            // Ident or keyword
            StringBuffer b = new StringBuffer();
            while(Character.isLetterOrDigit(c)) {
                b.append(c);
                c = getch();
            }
            ungetch();
            String val = b.toString();
            Token.Type t = keywords.get(val);
            return new Token(t == null ? Token.Type.IDENT : t, line, col, val);
        } else if (Character.isDigit(c)) {
            StringBuffer b = new StringBuffer();
            while(Character.isDigit(c)) {
                b.append(c);
                c = getch();
            }
            if (c == '.') {
                b.append(c);
                c = getch();
                while(Character.isDigit(c)) {
                    b.append(c);
                    c = getch();
                }
            }
            ungetch();
            return new Token(Token.Type.NUM_LIT, line, col, b.toString());
        } else {
            switch(c) {
                case '"':
                case '\'': {
                    StringBuffer b = new StringBuffer();
                    char terminator = c;
                    c = getch();
                    while (c != terminator && c != 0) {
                        b.append(c);
                        c = getch();
                    }
                    return new Token(Token.Type.STRING_LIT, line, col, b.toString());
                }
                case '(': return new Token(Token.Type.LPAREN, line, col);
                case ')': return new Token(Token.Type.RPAREN, line, col);
                case '[': return new Token(Token.Type.LBRACK, line, col);
                case ']': return new Token(Token.Type.RBRACK, line, col);
                case '+': return new Token(Token.Type.PLUS, line, col);
                case '-': return new Token(Token.Type.MINUS, line, col);
                case '*': return new Token(Token.Type.ASTERISK, line, col);
                case '/': return new Token(Token.Type.SLASH, line, col);
                case '<': {
                    char c2 = getch();
                    if (c2 == '=') {
                        return new Token(Token.Type.LE, line, col);
                    } else if (c2 == '>') {
                        return new Token(Token.Type.NE, line, col);
                    } else {
                        ungetch();
                        return new Token(Token.Type.LT, line, col);
                    }
                }
                case '>': {
                    char c2 = getch();
                    if (c2 == '=') {
                        return new Token(Token.Type.GE, line, col);
                    } else {
                        ungetch();
                        return new Token(Token.Type.GT, line, col);
                    }
                }
                case '=': return new Token(Token.Type.EQ, line, col);
                case ',': return new Token(Token.Type.COMMA, line, col);
                case 0: return new Token(Token.Type.EOT, line, col);
            }
            return new Token(Token.Type.UNKNOWN, line, col, String.valueOf(c));
        }
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
