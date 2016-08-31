package com.asigner.kidpython.runtime;

import com.asigner.kidpython.compiler.Position;

public class ExecutionException extends Error {
    private final Position pos;
    private final String message;

    public ExecutionException(String message) {
        super();
        this.pos = new Position(0,0);
        this.message = message;
    }

    public ExecutionException(Position pos, String message) {
        super();
        this.pos = pos;
        this.message = String.format("On line %d, column %d: %s", pos.getLine(), pos.getCol(), message);
    }

    public Position getPos() {
        return pos;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
