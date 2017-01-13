package com.asigner.kidpython.ide.editor;

import com.asigner.kidpython.ide.Settings;
import com.google.common.collect.Maps;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

public class StylesheetRepository {

    private final Map<String, Stylesheet> stylesheets;

    public StylesheetRepository() {
        stylesheets = Maps.newHashMap();
    }

    public static File getThemeDirectory() {
        return new File(Settings.getSettingsDirectory() + "/themes");
    }

    public void loadDefaults() {
        // First, add the hard coded ones
        for (Stylesheet s : Stylesheet.ALL) {
            stylesheets.put(s.getName(), s);
        }

        // Now, load all TextMate files
        File dir = getThemeDirectory();
        dir.mkdir();
        File[] themes = dir.listFiles();
        if (themes != null) {
            for (File theme : themes) {
                try {
                    Stylesheet stylesheet = Stylesheet.loadTextMateTheme(new FileInputStream(theme));
                    String name = stylesheet.getName();
                    int cnt = 2;
                    while (stylesheets.containsKey(name)) {
                        name = String.format("%s_%d",stylesheet.getName(), cnt++);
                    }
                    stylesheets.put(name, stylesheet);
                } catch (IOException e) {
                    System.err.println("Can't load TextMate theme " + theme.getAbsolutePath());
                    e.printStackTrace();
                }
            }
        }
    }

    public StylesheetRepository replaceWith(StylesheetRepository toClone) {
        stylesheets.clear();
        stylesheets.putAll(toClone.stylesheets);
        return this;
    }

    public Stylesheet get(String name) {
        Stylesheet res = stylesheets.get(name);
        if (res == null) {
            res = getDefault();
        }
        return res;
    }

    public List<Stylesheet> getAll() {
        return stylesheets.values().stream()
                .sorted(Comparator.comparing(Stylesheet::getName))
                .collect(toList());
    }

    public Stylesheet getDefault() {
        return get(stylesheets.values().stream().map(Stylesheet::getName).findFirst().get());

    }
}
