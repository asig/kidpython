// Copyright 2016 Andreas Signer. All rights reserved.

package com.asigner.kidpython.controls.editor;

import com.google.common.collect.Maps;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

import java.util.HashMap;
import java.util.Map;

//import static com.asigner.kidpython.controls.editor..CodeLineStyler.TokenCOMMENT;
//import static com.asigner.kidpython.controls.editor.CodeLineStyler.Token.IDENT;
//import static com.asigner.kidpython.controls.editor.CodeLineStyler.Token.KEYWORD;
//import static com.asigner.kidpython.controls.editor.CodeLineStyler.Token.NUMBER;
//import static com.asigner.kidpython.controls.editor.CodeLineStyler.Token.OTHER;
//import static com.asigner.kidpython.controls.editor.CodeLineStyler.Token.STRING;

public class Stylesheet {

    public static final Stylesheet BORLAND = new Stylesheet("borland")
            .setDefaultBackground(new RGB(255,255,255))
            .setStyle(CodeLineStyler.Token.IDENT,   new RGB( 49,  49,  49), null, SWT.NONE)
            .setStyle(CodeLineStyler.Token.KEYWORD, new RGB(  0,   0, 123), null, SWT.BOLD)
            .setStyle(CodeLineStyler.Token.COMMENT, new RGB(  0, 131,   0), null, SWT.ITALIC)
            .setStyle(CodeLineStyler.Token.STRING,  new RGB(  0,   0, 245), null, SWT.NONE)
            .setStyle(CodeLineStyler.Token.NUMBER,  new RGB(  0,   0, 245), null, SWT.NONE)
            .setStyle(CodeLineStyler.Token.OTHER,   new RGB( 49,  49,  49), null, SWT.NONE);

    public static final Stylesheet MONOKAI_SUBLIME = new Stylesheet("monokai-sublime")
            .setDefaultBackground(new RGB( 34, 35, 30))
            .setStyle(CodeLineStyler.Token.IDENT,   new RGB(252, 252, 251), null, SWT.NONE)
            .setStyle(CodeLineStyler.Token.KEYWORD, new RGB(239,  37, 110), null, SWT.BOLD)
            .setStyle(CodeLineStyler.Token.NUMBER,  new RGB(167, 124, 245), null, SWT.NONE)
            .setStyle(CodeLineStyler.Token.STRING,  new RGB(221, 210, 111), null, SWT.NONE)
            .setStyle(CodeLineStyler.Token.COMMENT, new RGB(112, 109,  77), null, SWT.ITALIC)
            .setStyle(CodeLineStyler.Token.OTHER,   new RGB(252, 252, 251), null, SWT.NONE);


    static class Style {
        private final Color fg;
        private final Color bg;
        private final int fontStyle;

        public Style(Color fg, Color bg, int fontStyle) {
            this.fg = fg;
            this.bg = bg;
            this.fontStyle = fontStyle;
        }

        public Color getBg() {
            return bg;
        }

        public Color getFg() {
            return fg;
        }

        public int getFontStyle() {
            return fontStyle;
        }
    }

    private final HashMap<RGB, Color> colors = Maps.newHashMap();
    private final Map<CodeLineStyler.Token, Style> tokenStyles = Maps.newHashMap();
    private final String name;

    private Color background = Display.getDefault().getSystemColor(SWT.COLOR_WHITE);

    public Stylesheet(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Color getDefaultBackground() {
        return background;
    }

    public Stylesheet setDefaultBackground(RGB rgb) {
        background = getColor(rgb);
        return this;
    }

    public Style getStyle(CodeLineStyler.Token token) {
        Style style = tokenStyles.get(token);
        if (style == null) {
            style = new Style(Display.getDefault().getSystemColor(SWT.COLOR_BLACK), background, SWT.NONE);
            tokenStyles.put(token, style);
        }
        return style;
    }

    public Stylesheet setStyle(CodeLineStyler.Token token, RGB foreground, RGB background, int fontStyle) {
        Style style = new Style(getColor(foreground), getColor(background), fontStyle);
        tokenStyles.put(token, style);
        return this;
    }

    public void dispose() {
        for (Color c: colors.values()) {
            c.dispose();
        }
    }

    private Color getColor(RGB rgb) {
        if (rgb == null) {
            return null;
        }
        Color c = colors.get(rgb);
        if (c == null) {
            c = new Color(Display.getDefault(), rgb);
            colors.put(rgb, c);
        }
        return c;
    }
}
