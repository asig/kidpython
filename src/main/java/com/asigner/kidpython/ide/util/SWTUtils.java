package com.asigner.kidpython.ide.util;

/**
 * Created by asigner on 15/10/16.
 */
public class SWTUtils {

    public static final double FONT_SCALE;

    static {
        String os = System.getProperty("os.name").split(" ")[0];
        if ("Mac".equals(os)) {
            FONT_SCALE = 1.6;
        } else {
            FONT_SCALE = 1.0;
        }
    }

    public static int scaleFont(int size) {
        return (int)(size * FONT_SCALE);
    }
}
