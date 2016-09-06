package com.asigner.kidpython.compiler;

import java.util.Set;

import static java.util.stream.Collectors.joining;

public class Error {

    enum Code {
        UNEXPECTED_TOKEN,
        RETURN_NOT_ALLOWED_OUTSIDE_FUNCTION
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

    public static Error returnNotAllowedOutsideFunction(Position pos) {
        return new Error(
                Code.RETURN_NOT_ALLOWED_OUTSIDE_FUNCTION,
                "return statement not allowed outside a function",
                pos);
    }

    private Error(Code code, String message, Position pos) {
        this.code = code;
        this.message = message;
        this.pos = pos;
    }

    public String toString() {
        return String.format("Line %d, column %d: (%s) %s", pos.getLine(), pos.getCol(), code, message);

    }

    public Position getPos() {
        return pos;
    }
}
