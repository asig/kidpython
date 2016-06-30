package com.asigner.kidpython.compiler;

import java.util.Set;

import static java.util.stream.Collectors.joining;

public class Error {

    enum Code {
        UNEXPECTED_TOKEN,
    }

    private final Position pos;
    private final Code code;
    private final String message;

    public static Error unexpectedToken(Token token, Set<Token.Type> expected) {
        return new Error(
                Code.UNEXPECTED_TOKEN,
                "Unexpected token " + token.getType() + ". Expected instead one of " + expected.stream().map(Enum::toString).collect(joining(",")),
                token.getPos());
    }

    private Error(Code code, String message, Position pos) {
        this.code = code;
        this.message = message;
        this.pos = pos;
    }
}
