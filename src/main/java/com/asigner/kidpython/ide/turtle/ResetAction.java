package com.asigner.kidpython.ide.turtle;

public class ResetAction extends TurtleAction {

    public ResetAction(TurtleCanvas canvas) {
        super(canvas);
    }

    @Override
    public void run() {
        canvas.reset();
    }
}
