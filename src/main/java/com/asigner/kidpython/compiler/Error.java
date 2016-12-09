package com.asigner.kidpython.compiler;

import com.asigner.kidpython.util.Messages;

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

    private static String getTokenMsg(Token.Type type) {
        return Messages.get(Messages.Key.valueOf("Compiler_Token_" + type.toString()));
    }

    public static Error unexpectedToken(Token token, Set<Token.Type> expected) {
        String unexpectedTokenMsg = getTokenMsg(token.getType());
        String expectedMsg = expected.stream().map(Error::getTokenMsg).collect(joining(", "));
        return new Error(
                Code.UNEXPECTED_TOKEN,
                String.format(Messages.get(Messages.Key.Compiler_Error_UNEXPECTED_TOKEN), unexpectedTokenMsg, expectedMsg),
                token.getPos());
    }

    public static Error returnNotAllowedOutsideFunction(Position pos) {
        String message = Messages.get(Messages.Key.Compiler_Error_RETURN_NOT_ALLOWED_OUTSIDE_FUNCTION);
        return new Error(
                Code.RETURN_NOT_ALLOWED_OUTSIDE_FUNCTION,
                message,
                pos);
    }

    private Error(Code code, String message, Position pos) {
        this.code = code;
        this.message = message;
        this.pos = pos;
    }

    public String toString() {
        String posStr = String.format(Messages.get(Messages.Key.Compiler_Error_Position), pos.getLine(), pos.getCol());
        return String.format("%s: (%s) %s", posStr, code, message);
    }

    public Position getPos() {
        return pos;
    }
}
