// Copyright 2016 Andreas Signer. All rights reserved.

package com.asigner.kidpython.controls.editor;

import com.google.common.collect.ImmutableMap;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

import java.util.Map;

import static com.asigner.kidpython.controls.editor.CodeLineStyler.Token.COMMENT;
import static com.asigner.kidpython.controls.editor.CodeLineStyler.Token.IDENT;
import static com.asigner.kidpython.controls.editor.CodeLineStyler.Token.KEYWORD;
import static com.asigner.kidpython.controls.editor.CodeLineStyler.Token.NUMBER;
import static com.asigner.kidpython.controls.editor.CodeLineStyler.Token.OTHER;
import static com.asigner.kidpython.controls.editor.CodeLineStyler.Token.STRING;

public class Stylesheet {

    /* // "borland"
    private final Color[] colors = new Color[] {
            makeColor(255,255,255),
            makeColor( 49, 49, 49),
            makeColor(  0,  0,123),
            makeColor(  0,131,  0),
            makeColor(  0,  0,245)
    };

    private final Map<CodeLineStyler.Token, Style> tokenStyles = ImmutableMap.<CodeLineStyler.Token, Style>builder()
            .put(IDENT,   new Style(colors[1], null, SWT.NONE))
            .put(KEYWORD, new Style(colors[2], null, SWT.BOLD))
            .put(COMMENT, new Style(colors[3], null, SWT.ITALIC))
            .put(STRING,  new Style(colors[4], null, SWT.NONE))
            .put(NUMBER,  new Style(colors[4], null, SWT.NONE))
            .put(OTHER,   new Style(colors[1], null, SWT.NONE))
            .build();
    */

    // monokai-sublime
    private final Color[] colors = new Color[] {
            makeColor( 34, 35, 30),
            makeColor(252,252,251),
            makeColor(239, 37,110),
            makeColor(167,124,245),
            makeColor(221,210,111),
            makeColor(112,109, 77),


            makeColor(  0,131,  0),
            makeColor(  0,  0,245)
    };

    private final Map<CodeLineStyler.Token, Style> tokenStyles = ImmutableMap.<CodeLineStyler.Token, Style>builder()
            .put(IDENT,   new Style(colors[1], null, SWT.NONE))
            .put(KEYWORD, new Style(colors[2], null, SWT.BOLD))
            .put(NUMBER,  new Style(colors[3], null, SWT.NONE))
            .put(STRING,  new Style(colors[4], null, SWT.NONE))
            .put(COMMENT, new Style(colors[5], null, SWT.ITALIC))
            .put(OTHER,   new Style(colors[1], null, SWT.NONE))
            .build();



    public static class Style {
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

    public Color getDefaultBackground() {
        return colors[0];
    }

    public Style getStyle(CodeLineStyler.Token token) {
        return tokenStyles.get(token);
    }

    public void dispose() {
        for (Color c: colors) {
            c.dispose();
        }
    }

    private Color makeColor(int r, int g, int b) {
        return new Color(Display.getDefault(), new RGB(r,g,b));
    }
}
