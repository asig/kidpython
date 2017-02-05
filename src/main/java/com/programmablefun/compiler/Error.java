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

import com.programmablefun.util.Messages;

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
