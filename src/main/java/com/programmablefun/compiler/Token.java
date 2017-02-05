/*
 * Copyright (c) 2017 Andreas Signer <asigner@gmail.com>
 *
 * This file is part of programmablefun.
 *
 * programmablefun is free software: you can redistribute it and/or
 * modify it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * programmablefun is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with programmablefun.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.programmablefun.compiler;

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
