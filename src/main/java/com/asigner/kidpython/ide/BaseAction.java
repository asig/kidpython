// Copyright 2016 Andreas Signer. All rights reserved.

package com.asigner.kidpython.ide;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;

public class BaseAction extends Action {

    private final Runnable handler;

    public BaseAction(String text, Image image, Runnable handler) {
        super(text, ImageDescriptor.createFromImage((image)));
        this.handler = handler;
    }

    @Override
    public void run() {
        handler.run();
    }
}
