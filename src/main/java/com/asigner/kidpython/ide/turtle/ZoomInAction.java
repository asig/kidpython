package com.asigner.kidpython.ide.turtle;

public class ZoomInAction extends TurtleAction {

    public ZoomInAction(TurtleCanvas canvas) {
        super(canvas);
    }

    @Override
    public void run() {
        canvas.zoomIn();
    }
}
