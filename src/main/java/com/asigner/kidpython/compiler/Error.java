package com.asigner.kidpython.compiler;

import java.util.Set;

import static java.util.stream.Collectors.joining;

public class Error {

    enum Code {
        UNEXPECTED_TOKEN,
    }

    private final int line;
    private final int col;
    private final Code code;
    private final String message;

    public static Error unexpectedToken(Token token, Set<Token.Type> expected) {
        return new Error(
                Code.UNEXPECTED_TOKEN,
                "Unexpected token " + token.getType() + ". Expected instead one of " + expected.stream().map(Enum::toString).collect(joining(",")),
                token.getLine(),
                token.getCol());
    }

    private Error(Code code, String message, int line, int col) {
        this.code = code;
        this.message = message;
        this.line = line;
        this.col = col;
    }
}
