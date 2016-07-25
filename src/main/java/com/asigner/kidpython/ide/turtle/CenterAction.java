package com.asigner.kidpython.ide.turtle;

public class CenterAction extends TurtleAction {

    public CenterAction(TurtleCanvas canvas) {
        super(canvas);
    }

    @Override
    public void run() {
        canvas.setOffset(0, 0);
    }
}
