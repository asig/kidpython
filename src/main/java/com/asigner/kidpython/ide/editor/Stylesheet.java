// Copyright 2016 Andreas Signer. All rights reserved.

package com.asigner.kidpython.ide.editor;

import com.asigner.kidpython.ide.util.SWTResources;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.asigner.kidpython.ide.editor.Stylesheet.Entity.COMMENT;
import static com.asigner.kidpython.ide.editor.Stylesheet.Entity.IDENT;
import static com.asigner.kidpython.ide.editor.Stylesheet.Entity.KEYWORD;
import static com.asigner.kidpython.ide.editor.Stylesheet.Entity.LINE_NUMBER_BG;
import static com.asigner.kidpython.ide.editor.Stylesheet.Entity.LINE_NUMBER_FG;
import static com.asigner.kidpython.ide.editor.Stylesheet.Entity.NUMBER;
import static com.asigner.kidpython.ide.editor.Stylesheet.Entity.STRING;
import static com.asigner.kidpython.ide.editor.Stylesheet.Entity.WELL_KNOWN_STRING;
import static com.asigner.kidpython.ide.editor.Stylesheet.Entity.OTHER;

public class Stylesheet {
    // TO ADD:
    // https://studiostyl.es/schemes/borland-pascal-theme
    // http://enrmarc.github.io/atom-theme-gallery/

    enum Entity {
        IDENT,
        KEYWORD,
        COMMENT,
        STRING,
        NUMBER,
        WELL_KNOWN_STRING,
        OTHER,
        LINE_NUMBER_FG,
        LINE_NUMBER_BG
    }

    private static int parseHex(String hex) {
        return Integer.parseInt(hex, 16);
    }

    private static RGB parseColor(String s) {
        try {
            if (s.startsWith("#")) {
                String colStr = s.substring(1);
                if (colStr.length() == 3) {
                    String r = colStr.substring(0, 1);
                    String g = colStr.substring(1, 2);
                    String b = colStr.substring(2, 3);
                    return new RGB(parseHex(r + r), parseHex(g + g), parseHex(b + b));
                } else if (colStr.length() == 6) {
                    String r = colStr.substring(0, 2);
                    String g = colStr.substring(2, 4);
                    String b = colStr.substring(4, 6);
                    return new RGB(parseHex(r), parseHex(g), parseHex(b));
                } else {
                    System.err.println("Invald color code " + s);
                    return new RGB(0, 0, 0);
                }
            } else {
                // TODO(asigner): Add name based parsing.
                System.err.println("Invald color code " + s);
                return new RGB(0, 0, 0);
            }
        } catch (NumberFormatException e) {
            // TODO(asigner): Add name based parsing.
            System.err.println("Invald color code " + s);
            return new RGB(0, 0, 0);
        }
    }



    static class JsonStyle {
        private static final Map<String, Integer> nameToValue = ImmutableMap.<String, Integer>builder()
                .put("BOLD", SWT.BOLD)
                .put("ITALIC", SWT.ITALIC)
                .build();

        @JsonProperty("fg") String fg;
        @JsonProperty("bg") String bg;
        @JsonProperty("attrs") List<String> attrs;

        int parseAttrs() {
            int res = 0;
            for (String attr : attrs) {
                Integer i = nameToValue.get(attr);
                if (i == null) {
                    System.err.println("Unknown Attribute " + attr);
                    i = 0;
                }
                res |= i;
            }
            return res;
        }

        Style toStyle() {
            return new Style(
                    fg != null ? SWTResources.getColor(parseColor(fg)) : null,
                    bg != null ? SWTResources.getColor(parseColor(bg)) : null,
                    parseAttrs()
            );
        }
    }

    static class JsonStylesheet {
        private static final Style DEFAULT_STYLE = new Style(SWTResources.getColor(new RGB(0,0,0)), SWTResources.getColor(new RGB(255,255,255)), 0);

        @JsonProperty("name") String name;
        @JsonProperty("default_bg") String defaultBackground;
        @JsonProperty("IDENT") JsonStyle styleIdent;
        @JsonProperty("KEYWORD") JsonStyle styleKeyword;
        @JsonProperty("COMMENT") JsonStyle styleComment;
        @JsonProperty("STRING") JsonStyle styleString;
        @JsonProperty("NUMBER") JsonStyle styleNumber;
        @JsonProperty("WELL_KNOWN_STRING") JsonStyle styleWellKnownString;
        @JsonProperty("LINE_NUMBER_FG") JsonStyle styleLineNumberFg;
        @JsonProperty("LINE_NUMBER_BG") JsonStyle styleLineNumberBg;
        @JsonProperty("OTHER") JsonStyle styleOther;


        Stylesheet toStylesheet() {
            if (name == null) {
                name = "Stylesheet " + this.hashCode();
            }
            Stylesheet stylesheet = new Stylesheet(name);
            if (defaultBackground != null) {
                stylesheet.setDefaultBackground(parseColor(defaultBackground));
            } else {
                stylesheet.setDefaultBackground(new RGB(255,255,255));
            }

            stylesheet.setStyle(IDENT, styleIdent != null ? styleIdent.toStyle() : DEFAULT_STYLE);
            stylesheet.setStyle(KEYWORD, styleKeyword != null ? styleKeyword.toStyle() : DEFAULT_STYLE);
            stylesheet.setStyle(COMMENT, styleComment != null ? styleComment.toStyle() : DEFAULT_STYLE);
            stylesheet.setStyle(STRING, styleString != null ? styleString.toStyle() : DEFAULT_STYLE);
            stylesheet.setStyle(NUMBER, styleNumber != null ? styleNumber.toStyle() : DEFAULT_STYLE);
            stylesheet.setStyle(WELL_KNOWN_STRING, styleWellKnownString!= null ? styleWellKnownString.toStyle() : DEFAULT_STYLE);
            stylesheet.setStyle(LINE_NUMBER_FG, styleLineNumberFg != null ? styleLineNumberFg.toStyle() : DEFAULT_STYLE);
            stylesheet.setStyle(LINE_NUMBER_BG, styleLineNumberBg != null ? styleLineNumberBg.toStyle() : DEFAULT_STYLE);
            stylesheet.setStyle(OTHER, styleOther != null ? styleOther.toStyle() : DEFAULT_STYLE);

            return stylesheet;
        }
    }

    static class JsonStylesheets {
        @JsonProperty("stylesheets") List<JsonStylesheet> stylesheets;
    }

    public static final List<Stylesheet> ALL = loadJsonStyles();

    private static List<Stylesheet> loadJsonStyles() {
        InputStream is = Stylesheet.class.getResourceAsStream("stylesheets.json");
        ObjectMapper m = new ObjectMapper();
        JsonFactory factory = m.getFactory();
        try {
            JsonStylesheets jsonStylesheets = factory.createParser(is).readValueAs(JsonStylesheets.class);
            return jsonStylesheets.stylesheets.stream().map(JsonStylesheet::toStylesheet).collect(Collectors.toList());
        } catch (IOException e) {
            System.err.println("Can't load JSON styles!");
            e.printStackTrace();
            System.exit(1);
        }
        return null;
    }

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

    private final Map<Entity, Style> tokenStyles = Maps.newHashMap();
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

    public Style getStyle(Entity entity) {
        Style style = tokenStyles.get(entity);
        if (style == null) {
            style = new Style(Display.getDefault().getSystemColor(SWT.COLOR_BLACK), background, SWT.NONE);
            tokenStyles.put(entity, style);
        }
        return style;
    }

    public Stylesheet setStyle(Entity entity, Style style) {
        tokenStyles.put(entity, style);
        return this;
    }

    public void dispose() {
    }

}
