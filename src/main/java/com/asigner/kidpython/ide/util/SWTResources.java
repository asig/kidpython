package com.asigner.kidpython.ide.util;

import com.google.common.collect.Maps;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.RGBA;
import org.eclipse.swt.widgets.Display;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

public class SWTResources {

    private static Font elusiveFont;
    private static final Map<RGBA, Color> colors = Maps.newHashMap();

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
        try {
            Path tmpFont = Files.createTempFile("font", ".ttf");
            Files.copy(SWTResources.class.getResourceAsStream("/com/asigner/kidpython/ide/fonts/elusiveicons-webfont.ttf"), tmpFont, REPLACE_EXISTING);
            Display.getDefault().loadFont(tmpFont.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
