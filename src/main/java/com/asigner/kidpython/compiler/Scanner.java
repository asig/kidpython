package com.asigner.kidpython.compiler;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.ImmutableMap;

import java.util.Map;

public class Scanner {

    private static final Map<String, Token.Type> keywords = ImmutableMap.<String, Token.Type>builder()
            .put("function", Token.Type.FUNC)
            .put("case", Token.Type.CASE)
            .put("of", Token.Type.OF)
            .put("for", Token.Type.FOR)
            .put("end", Token.Type.END)
            .put("if", Token.Type.IF)
            .put("then", Token.Type.THEN)
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
        // Read over whitespace and comments

        char c;
        for(;;) {
            // Read over whitespace
            while (Character.isWhitespace(getch())) ;

            ungetch();
            c = getch();

            // Read over comment, if applicable
            if (c == '/') {
                char c2 = getch();
                if (c2 == '/') {
                    do { c2 = getch(); } while (c2 != '\n'  && c2 != 0);
                } else if (c2 == '*') {
                    while ( !((c == '*' && c2 == '/') || (c == 0 && c2 == 0))) {
                        c = c2;
                        c2 = getch();
                    }
                } else {
                    ungetch();
                    break;
                }
            } else {
                break;
            }
        }

        Position pos = getPos();
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
            return new Token(t == null ? Token.Type.IDENT : t, pos, val);
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
            return new Token(Token.Type.NUM_LIT, getPos(), b.toString());
        } else {
            switch(c) {
                case '"':
                case '\'': {
                    StringBuilder b = new StringBuilder();
                    char terminator = c;
                    c = getch();
                    while (c != terminator && c != 0) {
                        b.append(c);
                        c = getch();
                    }
                    return new Token(Token.Type.STRING_LIT, pos, b.toString());
                }
                case '|': return new Token(Token.Type.BAR, pos);
                case '(': return new Token(Token.Type.LPAREN, pos);
                case ')': return new Token(Token.Type.RPAREN, pos);
                case '[': return new Token(Token.Type.LBRACK, pos);
                case ']': return new Token(Token.Type.RBRACK, pos);
                case '{': return new Token(Token.Type.LBRACE, pos);
                case '}': return new Token(Token.Type.RBRACE, pos);
                case '+': return new Token(Token.Type.PLUS, pos);
                case '-': return new Token(Token.Type.MINUS, pos);
                case '*': return new Token(Token.Type.ASTERISK, pos);
                case '/': return new Token(Token.Type.SLASH, pos);
                case '<': {
                    char c2 = getch();
                    if (c2 == '=') {
                        return new Token(Token.Type.LE, pos);
                    } else if (c2 == '>') {
                        return new Token(Token.Type.NE, pos);
                    } else {
                        ungetch();
                        return new Token(Token.Type.LT, pos);
                    }
                }
                case '>': {
                    char c2 = getch();
                    if (c2 == '=') {
                        return new Token(Token.Type.GE, pos);
                    } else {
                        ungetch();
                        return new Token(Token.Type.GT, pos);
                    }
                }
                case '=': return new Token(Token.Type.EQ, pos);
                case ',': return new Token(Token.Type.COMMA, pos);
                case ':': return new Token(Token.Type.COLON, pos);
                case '.': {
                    char c2 = getch();
                    if (c2 == '.') {
                        return new Token(Token.Type.DOTDOT, pos);
                    } else {
                        ungetch();
                        return new Token(Token.Type.DOT, pos);
                    }
                }
                case 0: return new Token(Token.Type.EOT, pos);
            }
            return new Token(Token.Type.UNKNOWN, pos, String.valueOf(c));
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

    public Position getPos() {
        return new Position(line, col);
    }
}
