// Copyright 2016 Andreas Signer. All rights reserved.

package com.asigner.kidpython.ide.controls.editor;

import com.asigner.kidpython.ide.util.SWTResources;
import com.google.common.collect.Maps;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

import java.util.Map;

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

    public static final Stylesheet RAINBOW = new Stylesheet("rainbow")
            .setDefaultBackground(new RGB( 65, 67, 67))
            .setStyle(CodeLineStyler.Token.IDENT,   new RGB(193, 200, 208), null, SWT.NONE)
            .setStyle(CodeLineStyler.Token.KEYWORD, new RGB(188, 141, 188), null, SWT.BOLD)
            .setStyle(CodeLineStyler.Token.NUMBER,  new RGB(230, 134,  81), null, SWT.NONE)
            .setStyle(CodeLineStyler.Token.STRING,  new RGB(128, 176, 169), null, SWT.NONE)
            .setStyle(CodeLineStyler.Token.COMMENT, new RGB(138, 140, 138), null, SWT.ITALIC)
            .setStyle(CodeLineStyler.Token.OTHER,   new RGB(193, 200, 208), null, SWT.NONE);

    public static final Stylesheet DRACULA = new Stylesheet("dracula")
            .setDefaultBackground(new RGB( 37, 38, 50))
            .setStyle(CodeLineStyler.Token.IDENT,   new RGB(229, 229, 224), null, SWT.NONE)
            .setStyle(CodeLineStyler.Token.KEYWORD, new RGB(129, 215, 233), null, SWT.BOLD)
            .setStyle(CodeLineStyler.Token.NUMBER,  new RGB(223, 231, 130), null, SWT.NONE)
            .setStyle(CodeLineStyler.Token.STRING,  new RGB(223, 231, 130), null, SWT.NONE)
            .setStyle(CodeLineStyler.Token.COMMENT, new RGB( 50,  97, 158), null, SWT.ITALIC)
            .setStyle(CodeLineStyler.Token.OTHER,   new RGB(229, 229, 224), null, SWT.NONE);

    public static final Stylesheet BLACK_AND_WHITE = new Stylesheet("black-and-white")
            .setDefaultBackground(new RGB(255, 255, 255))
            .setStyle(CodeLineStyler.Token.IDENT,   new RGB(0, 0, 0), null, SWT.NONE)
            .setStyle(CodeLineStyler.Token.KEYWORD, new RGB(0, 0, 0), null, SWT.BOLD)
            .setStyle(CodeLineStyler.Token.NUMBER,  new RGB(0, 0, 0), null, SWT.NONE)
            .setStyle(CodeLineStyler.Token.STRING,  new RGB(0, 0, 0), null, SWT.NONE)
            .setStyle(CodeLineStyler.Token.COMMENT, new RGB(0, 0, 0), null, SWT.ITALIC)
            .setStyle(CodeLineStyler.Token.OTHER,   new RGB(0, 0, 0), null, SWT.NONE);

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
        background = SWTResources.getColor(rgb);
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
        Style style = new Style(SWTResources.getColor(foreground), SWTResources.getColor(background), fontStyle);
        tokenStyles.put(token, style);
        return this;
    }

    public void dispose() {
    }

}
