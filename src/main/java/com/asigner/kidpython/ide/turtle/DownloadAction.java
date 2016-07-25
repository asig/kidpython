package com.asigner.kidpython.ide.turtle;

import org.apache.batik.dom.GenericDOMImplementation;
import org.apache.batik.svggen.SVGGraphics2D;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

public class DownloadAction extends TurtleAction {

    public DownloadAction(TurtleCanvas canvas) {
        super(canvas);
    }

    @Override
    public void run() {

        DOMImplementation domImpl = GenericDOMImplementation.getDOMImplementation();
        Document document = domImpl.createDocument("http://www.w3.org/2000/svg", "svg", null);
        SVGGraphics2D svgGenerator = new SVGGraphics2D(document);

        canvas.drawUntransformed(svgGenerator);

        boolean useCSS = true; // we want to use CSS style attributes
        try (Writer out = new OutputStreamWriter(new FileOutputStream("/tmp/foo.svg"), "UTF-8")) {
            svgGenerator.stream(out, useCSS);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
