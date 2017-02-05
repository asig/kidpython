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

package com.programmablefun.ide.turtle;

import org.apache.batik.dom.GenericDOMImplementation;
import org.apache.batik.svggen.SVGGraphics2D;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
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

        FileDialog dialog = new FileDialog(canvas.getShell(), SWT.SAVE);
        dialog.setText("FOOBAR");
        dialog.setFilterNames(new String[] { "SVG files", "All Files (*.*)" });
        dialog.setFilterExtensions(new String[] { "*.svg", "*.*" }); // Windows
        String filename = dialog.open();
        if (filename != null) {
            DOMImplementation domImpl = GenericDOMImplementation.getDOMImplementation();
            Document document = domImpl.createDocument("http://www.w3.org/2000/svg", "svg", null);
            SVGGraphics2D svgGenerator = new SVGGraphics2D(document);

            canvas.drawUntransformed(svgGenerator);

            try (Writer out = new OutputStreamWriter(new FileOutputStream(filename), "UTF-8")) {
                svgGenerator.stream(out, true /* useCSS */);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
