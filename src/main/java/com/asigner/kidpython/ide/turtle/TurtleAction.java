package com.asigner.kidpython.ide.turtle;

import org.eclipse.jface.action.Action;

public class TurtleAction extends Action {

    protected final TurtleCanvas canvas;

    public TurtleAction(TurtleCanvas canvas) {
        this.canvas = canvas;
    }
}
