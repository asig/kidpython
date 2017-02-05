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

package com.programmablefun.ide.editor;

import com.programmablefun.ide.Settings;
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
        File dir = new File(Settings.getDataDirectory() + "/themes");
        dir.mkdirs();
        return dir;
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
