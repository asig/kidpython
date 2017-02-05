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

package com.programmablefun.ide.util;

import com.google.common.collect.Maps;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.RGBA;
import org.eclipse.swt.widgets.Display;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

public class SWTResources {

    private static final Map<RGBA, Color> colors = Maps.newHashMap();
    private static final Map<String, Image> images = Maps.newHashMap();

    public static Image getImage(String path) {
        Image img = images.get(path);
        if (img == null) {
            img = new Image(Display.getDefault(), SWTResources.class.getResourceAsStream(path));
            images.put(path, img);
        }
        return img;
    }

    public static Color getColor(RGB rgb) {
        if (rgb == null) {
            return null;
        }
        return getColor(new RGBA(rgb.red, rgb.green, rgb.blue, 255));
    }

    public static Color getColor(RGBA rgba) {
        if (rgba == null) {
            return null;
        }
        Color c = colors.get(rgba);
        if (c == null) {
            c = new Color(Display.getDefault(), rgba);
            colors.put(rgba, c);
        }
        return c;
    }

    static {
        loadFont("/com/asigner/kidpython/ide/fonts/elusiveicons-webfont.ttf");
        loadFont("/com/asigner/kidpython/ide/fonts/UbuntuMono-Regular.ttf");
        loadFont("/com/asigner/kidpython/ide/fonts/UbuntuMono-Bold.ttf");
        loadFont("/com/asigner/kidpython/ide/fonts/UbuntuMono-Italic.ttf");
        loadFont("/com/asigner/kidpython/ide/fonts/UbuntuMono-BoldItalic.ttf");
        loadFont("/com/asigner/kidpython/ide/fonts/RobotoMono-Regular.ttf");
        loadFont("/com/asigner/kidpython/ide/fonts/RobotoMono-Bold.ttf");
        loadFont("/com/asigner/kidpython/ide/fonts/RobotoMono-Italic.ttf");
        loadFont("/com/asigner/kidpython/ide/fonts/RobotoMono-BoldItalic.ttf");
    }

    private static void loadFont(String path) {
        try {
            Path tmpFont = Files.createTempFile("font", ".ttf");
            Files.copy(SWTResources.class.getResourceAsStream(path), tmpFont, REPLACE_EXISTING);
            Display.getDefault().loadFont(tmpFont.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
