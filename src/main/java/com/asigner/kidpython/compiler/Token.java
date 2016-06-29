package com.asigner.kidpython.compiler;

public class Token {

    public enum Type {
        FUNC,
        END,
        IF,
        THEN,
        ELSEIF,
        ELSE,
        FOR,
        STEP,
        IN,
        TO,
        DO,
        WHILE,
        REPEAT,
        UNTIL,
        RETURN,
        AND,
        OR,

        PLUS,
        MINUS,
        ASTERISK,
        SLASH,
        LPAREN,
        RPAREN,
        LBRACK,
        RBRACK,
        COMMA,

        EQ,
        NE,
        LE,
        LT,
        GE,
        GT,

        IDENT,
        STRING_LIT,
        NUM_LIT,

        EOT,
        UNKNOWN
    }

    private final Type type;
    private final int line, col;
    private final String val;

    Token(Type t, int line, int col) {
        this(t, line, col, "");
    }

    Token(Type t, int line, int col, String val) {
        this.type = t;
        this.line = line;
        this.col = col;
        this.val = val;
    }

    public int getCol() {
        return col;
    }

    public int getLine() {
        return line;
    }

    public Type getType() {
        return type;
    }

    public String getValue() {
        return val;
    }
}
