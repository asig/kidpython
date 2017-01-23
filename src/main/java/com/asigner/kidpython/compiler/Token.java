package com.asigner.kidpython.compiler;

public class Token {

    public enum Type {
        CASE,
        OF,
        FUNC,
        END,
        IF,
        THEN,
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
        LBRACE,
        RBRACE,
        COMMA,
        DOT,
        DOTDOT,
        COLON,
        BAR,

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
    private final Position pos;
    private final String val;

    Token(Type t, Position pos) {
        this(t, pos, "");
    }

    Token(Type t, Position pos, String val) {
        this.type = t;
        this.pos = pos;
        this.val = val;
    }

    public Position getPos() {
        return pos;
    }

    public Type getType() {
        return type;
    }

    public String getValue() {
        return val;
    }
}
