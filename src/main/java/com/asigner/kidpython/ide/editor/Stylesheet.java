// Copyright 2016 Andreas Signer. All rights reserved.

package com.asigner.kidpython.ide.editor;

import com.asigner.kidpython.ide.util.SWTResources;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
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
import static com.asigner.kidpython.ide.editor.Stylesheet.Entity.NUMBER;
import static com.asigner.kidpython.ide.editor.Stylesheet.Entity.OTHER;
import static com.asigner.kidpython.ide.editor.Stylesheet.Entity.STRING;
import static com.asigner.kidpython.ide.editor.Stylesheet.Entity.WELL_KNOWN_STRING;

public class Stylesheet {
    // TO ADD:
    // https://studiostyl.es/schemes/borland-pascal-theme
    // http://enrmarc.github.io/atom-theme-gallery/
    // http://colorsublime.com/
    // http://netbeansthemes.com/

    enum Entity {
        IDENT,
        KEYWORD,
        COMMENT,
        STRING,
        NUMBER,
        WELL_KNOWN_STRING,
        OTHER
    }

    private static final Map<String, RGB> SVG_COLORS = ImmutableMap.<String, RGB>builder()
            .put("aliceblue", new RGB(240, 248, 255))
            .put("antiquewhite", new RGB(250, 235, 215))
            .put("aqua", new RGB( 0, 255, 255))
            .put("aquamarine", new RGB(127, 255, 212))
            .put("azure", new RGB(240, 255, 255))
            .put("beige", new RGB(245, 245, 220))
            .put("bisque", new RGB(255, 228, 196))
            .put("black", new RGB( 0, 0, 0))
            .put("blanchedalmond", new RGB(255, 235, 205))
            .put("blue", new RGB( 0, 0, 255))
            .put("blueviolet", new RGB(138, 43, 226))
            .put("brown", new RGB(165, 42, 42))
            .put("burlywood", new RGB(222, 184, 135))
            .put("cadetblue", new RGB( 95, 158, 160))
            .put("chartreuse", new RGB(127, 255, 0))
            .put("chocolate", new RGB(210, 105, 30))
            .put("coral", new RGB(255, 127, 80))
            .put("cornflowerblue", new RGB(100, 149, 237))
            .put("cornsilk", new RGB(255, 248, 220))
            .put("crimson", new RGB(220, 20, 60))
            .put("cyan", new RGB( 0, 255, 255))
            .put("darkblue", new RGB( 0, 0, 139))
            .put("darkcyan", new RGB( 0, 139, 139))
            .put("darkgoldenrod", new RGB(184, 134, 11))
            .put("darkgray", new RGB(169, 169, 169))
            .put("darkgreen", new RGB( 0, 100, 0))
            .put("darkgrey", new RGB(169, 169, 169))
            .put("darkkhaki", new RGB(189, 183, 107))
            .put("darkmagenta", new RGB(139, 0, 139))
            .put("darkolivegreen", new RGB( 85, 107, 47))
            .put("darkorange", new RGB(255, 140, 0))
            .put("darkorchid", new RGB(153, 50, 204))
            .put("darkred", new RGB(139, 0, 0))
            .put("darksalmon", new RGB(233, 150, 122))
            .put("darkseagreen", new RGB(143, 188, 143))
            .put("darkslateblue", new RGB( 72, 61, 139))
            .put("darkslategray", new RGB( 47, 79, 79))
            .put("darkslategrey", new RGB( 47, 79, 79))
            .put("darkturquoise", new RGB( 0, 206, 209))
            .put("darkviolet", new RGB(148, 0, 211))
            .put("deeppink", new RGB(255, 20, 147))
            .put("deepskyblue", new RGB( 0, 191, 255))
            .put("dimgray", new RGB(105, 105, 105))
            .put("dimgrey", new RGB(105, 105, 105))
            .put("dodgerblue", new RGB( 30, 144, 255))
            .put("firebrick", new RGB(178, 34, 34))
            .put("floralwhite", new RGB(255, 250, 240))
            .put("forestgreen", new RGB( 34, 139, 34))
            .put("fuchsia", new RGB(255, 0, 255))
            .put("gainsboro", new RGB(220, 220, 220))
            .put("ghostwhite", new RGB(248, 248, 255))
            .put("gold", new RGB(255, 215, 0))
            .put("goldenrod", new RGB(218, 165, 32))
            .put("gray", new RGB(128, 128, 128))
            .put("grey", new RGB(128, 128, 128))
            .put("green", new RGB( 0, 128, 0))
            .put("greenyellow", new RGB(173, 255, 47))
            .put("honeydew", new RGB(240, 255, 240))
            .put("hotpink", new RGB(255, 105, 180))
            .put("indianred", new RGB(205, 92, 92))
            .put("indigo", new RGB( 75, 0, 130))
            .put("ivory", new RGB(255, 255, 240))
            .put("khaki", new RGB(240, 230, 140))
            .put("lavender", new RGB(230, 230, 250))
            .put("lavenderblush", new RGB(255, 240, 245))
            .put("lawngreen", new RGB(124, 252, 0))
            .put("lemonchiffon", new RGB(255, 250, 205))
            .put("lightblue", new RGB(173, 216, 230))
            .put("lightcoral", new RGB(240, 128, 128))
            .put("lightcyan", new RGB(224, 255, 255))
            .put("lightgoldenrodyellow", new RGB(250, 250, 210))
            .put("lightgray", new RGB(211, 211, 211))
            .put("lightgreen", new RGB(144, 238, 144))
            .put("lightgrey", new RGB(211, 211, 211))
            .put("lightpink", new RGB(255, 182, 193))
            .put("lightsalmon", new RGB(255, 160, 122))
            .put("lightseagreen", new RGB( 32, 178, 170))
            .put("lightskyblue", new RGB(135, 206, 250))
            .put("lightslategray", new RGB(119, 136, 153))
            .put("lightslategrey", new RGB(119, 136, 153))
            .put("lightsteelblue", new RGB(176, 196, 222))
            .put("lightyellow", new RGB(255, 255, 224))
            .put("lime", new RGB( 0, 255, 0))
            .put("limegreen", new RGB( 50, 205, 50))
            .put("linen", new RGB(250, 240, 230))
            .put("magenta", new RGB(255, 0, 255))
            .put("maroon", new RGB(128, 0, 0))
            .put("mediumaquamarine", new RGB(102, 205, 170))
            .put("mediumblue", new RGB( 0, 0, 205))
            .put("mediumorchid", new RGB(186, 85, 211))
            .put("mediumpurple", new RGB(147, 112, 219))
            .put("mediumseagreen", new RGB( 60, 179, 113))
            .put("mediumslateblue", new RGB(123, 104, 238))
            .put("mediumspringgreen", new RGB( 0, 250, 154))
            .put("mediumturquoise", new RGB( 72, 209, 204))
            .put("mediumvioletred", new RGB(199, 21, 133))
            .put("midnightblue", new RGB( 25, 25, 112))
            .put("mintcream", new RGB(245, 255, 250))
            .put("mistyrose", new RGB(255, 228, 225))
            .put("moccasin", new RGB(255, 228, 181))
            .put("navajowhite", new RGB(255, 222, 173))
            .put("navy", new RGB( 0, 0, 128))
            .put("oldlace", new RGB(253, 245, 230))
            .put("olive", new RGB(128, 128, 0))
            .put("olivedrab", new RGB(107, 142, 35))
            .put("orange", new RGB(255, 165, 0))
            .put("orangered", new RGB(255, 69, 0))
            .put("orchid", new RGB(218, 112, 214))
            .put("palegoldenrod", new RGB(238, 232, 170))
            .put("palegreen", new RGB(152, 251, 152))
            .put("paleturquoise", new RGB(175, 238, 238))
            .put("palevioletred", new RGB(219, 112, 147))
            .put("papayawhip", new RGB(255, 239, 213))
            .put("peachpuff", new RGB(255, 218, 185))
            .put("peru", new RGB(205, 133, 63))
            .put("pink", new RGB(255, 192, 203))
            .put("plum", new RGB(221, 160, 221))
            .put("powderblue", new RGB(176, 224, 230))
            .put("purple", new RGB(128, 0, 128))
            .put("red", new RGB(255, 0, 0))
            .put("rosybrown", new RGB(188, 143, 143))
            .put("royalblue", new RGB( 65, 105, 225))
            .put("saddlebrown", new RGB(139, 69, 19))
            .put("salmon", new RGB(250, 128, 114))
            .put("sandybrown", new RGB(244, 164, 96))
            .put("seagreen", new RGB( 46, 139, 87))
            .put("seashell", new RGB(255, 245, 238))
            .put("sienna", new RGB(160, 82, 45))
            .put("silver", new RGB(192, 192, 192))
            .put("skyblue", new RGB(135, 206, 235))
            .put("slateblue", new RGB(106, 90, 205))
            .put("slategray", new RGB(112, 128, 144))
            .put("slategrey", new RGB(112, 128, 144))
            .put("snow", new RGB(255, 250, 250))
            .put("springgreen", new RGB( 0, 255, 127))
            .put("steelblue", new RGB( 70, 130, 180))
            .put("tan", new RGB(210, 180, 140))
            .put("teal", new RGB( 0, 128, 128))
            .put("thistle", new RGB(216, 191, 216))
            .put("tomato", new RGB(255, 99, 71))
            .put("turquoise", new RGB( 64, 224, 208))
            .put("violet", new RGB(238, 130, 238))
            .put("wheat", new RGB(245, 222, 179))
            .put("white", new RGB(255, 255, 255))
            .put("whitesmoke", new RGB(245, 245, 245))
            .put("yellow", new RGB(255, 255, 0))
            .put("yellowgreen", new RGB(154, 205, 50))
            .build();

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
                RGB col = SVG_COLORS.get(s.toLowerCase());
                if (col == null) {
                    System.err.println("Unknown color name " + s);
                    col = new RGB(0, 0, 0);
                }
                return col;
            }
        } catch (NumberFormatException e) {
            // TODO(asigner): Add name based parsing.
            System.err.println("Invald color code " + s);
            return new RGB(0, 0, 0);
        }
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class JsonStyle {
        private static final Map<String, Integer> nameToValue = ImmutableMap.<String, Integer>builder()
                .put("BOLD", SWT.BOLD)
                .put("ITALIC", SWT.ITALIC)
                .put("UNDERLINE", 0) // Underline is not store in fontstyles!
                .build();

        @JsonProperty("fg") public String fg;
        @JsonProperty("bg") public String bg;
        @JsonProperty("attrs") public List<String> attrs;

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

        boolean isUnderline() {
            for (String attr : attrs) {
                if (attr.equals("UNDERLINE")) {
                    return true;
                }
            }
            return false;
        }

        Style toStyle() {
            return new Style(
                    fg != null ? SWTResources.getColor(parseColor(fg)) : null,
                    bg != null ? SWTResources.getColor(parseColor(bg)) : null,
                    parseAttrs(),
                    isUnderline()
            );
        }
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class JsonStylesheet {
        private static final Style DEFAULT_STYLE = new Style(SWTResources.getColor(new RGB(0,0,0)), SWTResources.getColor(new RGB(255,255,255)), 0, false);

        @JsonProperty("name") public String name;
        @JsonProperty("author") public String author;
        @JsonProperty("default_bg") public String defaultBackground;
        @JsonProperty("selection_bg") public String selectionBackground;
        @JsonProperty("selection_fg") public String selectionForeground;
        @JsonProperty("gutter_bg") public String gutterBackground;
        @JsonProperty("gutter_fg") public String gutterForeground;
        @JsonProperty("IDENT") public JsonStyle styleIdent;
        @JsonProperty("KEYWORD") public JsonStyle styleKeyword;
        @JsonProperty("COMMENT") public JsonStyle styleComment;
        @JsonProperty("STRING") public JsonStyle styleString;
        @JsonProperty("NUMBER") public JsonStyle styleNumber;
        @JsonProperty("WELL_KNOWN_STRING") public JsonStyle styleWellKnownString;
        @JsonProperty("OTHER") public JsonStyle styleOther;

        Stylesheet toStylesheet() {
            if (name == null) {
                name = "Stylesheet " + this.hashCode();
            }
            Stylesheet stylesheet = new Stylesheet(name);

            stylesheet.setStyle(IDENT, styleIdent != null ? styleIdent.toStyle() : DEFAULT_STYLE);
            stylesheet.setStyle(KEYWORD, styleKeyword != null ? styleKeyword.toStyle() : DEFAULT_STYLE);
            stylesheet.setStyle(COMMENT, styleComment != null ? styleComment.toStyle() : DEFAULT_STYLE);
            stylesheet.setStyle(STRING, styleString != null ? styleString.toStyle() : DEFAULT_STYLE);
            stylesheet.setStyle(NUMBER, styleNumber != null ? styleNumber.toStyle() : DEFAULT_STYLE);
            stylesheet.setStyle(WELL_KNOWN_STRING, styleWellKnownString!= null ? styleWellKnownString.toStyle() : DEFAULT_STYLE);
            stylesheet.setStyle(OTHER, styleOther != null ? styleOther.toStyle() : DEFAULT_STYLE);

            if (defaultBackground != null) {
                stylesheet.setDefaultBackground(parseColor(defaultBackground));
            }
            if (selectionBackground != null) {
                stylesheet.setSelectionBackground(parseColor(selectionBackground));
            }
            if (selectionForeground != null) {
                stylesheet.setSelectionForeground(parseColor(selectionForeground));
            }
            if (gutterBackground != null) {
                stylesheet.setGutterBackground(parseColor(gutterBackground));
            } else {
                stylesheet.setGutterBackground(stylesheet.getDefaultBackground().getRGB());
            }
            if (gutterForeground != null) {
                stylesheet.setGutterForeground(parseColor(gutterForeground));
            } else {
                stylesheet.setGutterForeground(stylesheet.getStyle(OTHER).getFg().getRGB());
            }

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

    public static Stylesheet loadTextMateTheme(InputStream tm) throws IOException {
        TextMateThemeLoader loader = new TextMateThemeLoader(tm);
        return loader.load();
    }

    static class Style {
        private final Color fg;
        private final Color bg;
        private final int fontStyle;
        private final boolean underline;

        public Style(Color fg, Color bg, int fontStyle, boolean underline) {
            this.fg = fg;
            this.bg = bg;
            this.fontStyle = fontStyle;
            this.underline = underline;
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

        public boolean getUnderline() {
            return underline;
        }
    }

    private final Map<Entity, Style> tokenStyles = Maps.newHashMap();
    private final String name;

    private Color background = Display.getDefault().getSystemColor(SWT.COLOR_WHITE);
    private Color selectionBackground = Display.getDefault().getSystemColor(SWT.COLOR_LIST_SELECTION);
    private Color selectionForeground = Display.getDefault().getSystemColor(SWT.COLOR_LIST_SELECTION_TEXT);
    private Color gutterBackground = Display.getDefault().getSystemColor(SWT.COLOR_WHITE);
    private Color gutterForeground = Display.getDefault().getSystemColor(SWT.COLOR_BLACK);

    public Stylesheet(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Color getDefaultBackground() {
        return background;
    }

    public Color getSelectionBackground() {
        return selectionBackground;
    }

    public Color getSelectionForeground() {
        return selectionForeground;
    }

    public Color getGutterBackground() {
        return gutterBackground;
    }

    public Color getGutterForeground() {
        return gutterForeground;
    }

    public Stylesheet setSelectionBackground(RGB rgb) {
        selectionBackground = SWTResources.getColor(rgb);
        return this;
    }

    public Stylesheet setSelectionForeground(RGB rgb) {
        selectionForeground = SWTResources.getColor(rgb);
        return this;
    }

    public Stylesheet setGutterBackground(RGB rgb) {
        gutterBackground = SWTResources.getColor(rgb);
        return this;
    }

    public Stylesheet setGutterForeground(RGB rgb) {
        gutterForeground = SWTResources.getColor(rgb);
        return this;
    }

    public Stylesheet setDefaultBackground(RGB rgb) {
        background = SWTResources.getColor(rgb);
        return this;
    }

    public Style getStyle(Entity entity) {
        Style style = tokenStyles.get(entity);
        if (style == null) {
            style = new Style(Display.getDefault().getSystemColor(SWT.COLOR_BLACK), background, SWT.NONE, false);
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
