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

package com.programmablefun.tools;


import com.programmablefun.ide.editor.Stylesheet;
import com.programmablefun.ide.editor.TextMateThemeLoader;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import xmlwise.Plist;
import xmlwise.XmlParseException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toSet;

public class TextMateThemeConverter {

    private Stylesheet.JsonStylesheet stylesheet;
    private Map<String, Object> plist;

    public static void main(String ... args) {
        try {

            List<Stylesheet.JsonStylesheet> stylesheets = Files.list(new File(args[0]).toPath())
                    .map(TextMateThemeConverter::load)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
            ObjectMapper mapper = new ObjectMapper();
            mapper.writerWithDefaultPrettyPrinter().writeValue(System.out, stylesheets);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Stylesheet.JsonStylesheet load(Path path) {
        try {
            return new TextMateThemeLoader(new FileInputStream(path.toFile())).loadJson();
        } catch (IOException e) {
            System.err.println("Can't convert " + path.toString());
            return null;
        }
    }

    public TextMateThemeConverter(Path path) {
        this.stylesheet = new Stylesheet.JsonStylesheet();
        String content = "";
        try {
            // We need to do some preprocessing to get rid of XML-breaking comments...
            content = removeXmlComments(new String(Files.readAllBytes(path)));
            this.plist = Plist.fromXml(content);
        } catch (XmlParseException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String removeXmlComments(String s) {
        String res = "";
        int lastEnd = 0;
        int start = s.indexOf("<!--", lastEnd);
        while (start > -1) {
            res += s.substring(lastEnd, start);
            lastEnd = s.indexOf("-->", start) + 3;
            start = s.indexOf("<!--", lastEnd);
        }
        res += s.substring(lastEnd);
        return res.trim();
    }

    private Stylesheet.JsonStylesheet convert() {
        this.stylesheet.author = (String)plist.get("author");
        this.stylesheet.name = (String)plist.get("name");
        List<Map<String, Object>> entries = (List<Map<String, Object>>)plist.get("settings");
        for (Map<String, Object> entry : entries) {
            convertEntry(entry);
        }

        if (stylesheet.styleComment == null) {
            stylesheet.styleComment = stylesheet.styleOther;
        }
        if (stylesheet.styleString == null) {
            stylesheet.styleString = stylesheet.styleOther;
        }
        if (stylesheet.styleKeyword == null) {
            stylesheet.styleKeyword = stylesheet.styleOther;
        }
        if (stylesheet.styleIdent == null) {
            stylesheet.styleIdent = stylesheet.styleOther;
        }
        if (stylesheet.styleNumber == null) {
            stylesheet.styleNumber = stylesheet.styleOther;
        }
        if (stylesheet.styleWellKnownString == null) {
            stylesheet.styleWellKnownString = stylesheet.styleOther;
        }
        fixup(stylesheet.styleComment);
        fixup(stylesheet.styleString);
        fixup(stylesheet.styleKeyword);
        fixup(stylesheet.styleIdent);
        fixup(stylesheet.styleNumber);
        fixup(stylesheet.styleWellKnownString);

        return stylesheet;
    }

    private void fixup(Stylesheet.JsonStyle style) {
        if (style.fg == null) {
            style.fg = stylesheet.styleOther.fg;
        }
    }

    private void convertEntry(Map<String, Object> entry) {
        String rawScopes =(String) entry.get("scope");
        if (rawScopes == null) {
            Map<String, Object> settings = (Map<String, Object>)entry.get("settings");
            this.stylesheet.selectionBackground = (String)settings.get("selection");
            this.stylesheet.defaultBackground = (String)settings.get("background");
            this.stylesheet.gutterForeground = (String)settings.get("gutterForeground");
            this.stylesheet.gutterBackground = (String)settings.get("gutter");
            // Not used: caret, invisibles, lineHighlight, gutterForeground, gutter

            stylesheet.styleOther = new Stylesheet.JsonStyle();
            stylesheet.styleOther.attrs = Lists.newArrayList();
            stylesheet.styleOther.fg = (String)settings.get("foreground");
        } else {
            Set<String> scopes = Arrays.stream(rawScopes.split(","))
                    .map(String::trim)
                    .collect(toSet());
            Stylesheet.JsonStyle style = convertStyle((Map<String, Object>) entry.get("settings"));
            if (scopes.contains("comment")) {
                stylesheet.styleComment = style;
            } else if (scopes.contains("string")) {
                stylesheet.styleString = style;
            } else if (scopes.contains("keyword")) {
                stylesheet.styleKeyword = style;
            } else if (scopes.contains("variable")) {
                stylesheet.styleIdent = style;
            } else if (scopes.contains("constant.numeric")) {
                stylesheet.styleNumber = style;
            } else if (scopes.contains("support.function")) {
                stylesheet.styleWellKnownString = style;
            }
        }
    }

    private Stylesheet.JsonStyle convertStyle(Map<String, Object> settings) {
        Stylesheet.JsonStyle style = new Stylesheet.JsonStyle();
        if (settings == null) {
            return style;
        }
        style.fg = (String)settings.get("foreground");
        style.bg = (String)settings.get("background");
        style.attrs = Lists.newArrayList();
        Set<String> fontStyles = Sets.newHashSet(Strings.nullToEmpty( (String)settings.get("fontStyle")).split(" +"));
        for (String s : fontStyles) {
            switch (s) {
                case "underline": style.attrs.add("UNDERLINE"); break;
                case "bold": style.attrs.add("BOLD"); break;
                case "italic": style.attrs.add("ITALIC"); break;
                case "": break;
                default: System.err.println("Unsupport fontStyle attribute '" + s + "'");
            }
        }
        return style;
    }
}
