// Copyright 2016 Andreas Signer. All rights reserved.

package com.asigner.kidpython.tools;


import com.asigner.kidpython.ide.editor.Stylesheet;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.RuntimeJsonMappingException;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import xmlwise.Plist;
import xmlwise.XmlParseException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toSet;

public class TextMateThemeConverter {

    private Stylesheet.JsonStylesheet stylesheet;
    private Map<String, Object> plist;

    public static void main(String ... args) {
        try {
            List<Stylesheet.JsonStylesheet> stylesheets = Files.list(new File(args[0]).toPath())
                    .map(path -> new TextMateThemeConverter(path.toFile()).convert())
                    .collect(Collectors.toList());
            ObjectMapper mapper = new ObjectMapper();
            mapper.writerWithDefaultPrettyPrinter().writeValue(System.out, stylesheets);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public TextMateThemeConverter(File file) {
        this.stylesheet = new Stylesheet.JsonStylesheet();
        try {
            this.plist = Plist.load(file);
        } catch (XmlParseException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Stylesheet.JsonStylesheet convert() {
        this.stylesheet.author = (String)plist.get("author");
        this.stylesheet.name = (String)plist.get("name");
        List<Map<String, Object>> entries = (List<Map<String, Object>>)plist.get("settings");
        for (Map<String, Object> entry : entries) {
            convertEntry(entry);
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
