// Copyright 2016 Andreas Signer. All rights reserved.

package com.asigner.kidpython.ide;

import com.asigner.kidpython.ide.turtle.Turtle;

import java.awt.Canvas;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class AppAwt  extends Canvas {

    public static void main(String[] args) {
        Frame f=new Frame("Draw shape and text on Canvas");
        Canvas canvas=new AppAwt();

        f.add(canvas);

        f.setSize(300,300);
        f.setVisible(true);
        f.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent event) {
                System.exit(0);
            }
        });

    }

    public void paint(Graphics g) {
        Graphics2D g2=(Graphics2D)g;

        g2.drawString("Draw a rectangle", 100,100);
        g2.drawRect(100,200,50,50);

        Turtle.paint(g2);
    }
}

