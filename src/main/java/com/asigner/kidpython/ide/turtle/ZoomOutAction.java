package com.asigner.kidpython.ide.turtle;

public class ZoomOutAction extends TurtleAction {

    public ZoomOutAction(TurtleCanvas canvas) {
        super(canvas);
    }

    @Override
    public void run() {
        canvas.zoomOut();
    }
}
