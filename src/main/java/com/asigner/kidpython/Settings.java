package com.asigner.kidpython;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class Settings {
    private static final int SOURCES = 10;

    @JsonProperty("selected_source")
    private int selectedSource;

    @JsonProperty("sources")
    private List<Source> sources;

    private Settings() {
        selectedSource = 0;
        sources = Lists.newArrayListWithExpectedSize(SOURCES);
        for (int i = 0; i < SOURCES; i++) {
            sources.add(new Source(String.format("%d", i+1), ""));
        }
    }

    public static Settings load() {
        Settings settings = new Settings();
        File f = new File(getFileName());
        if (f.exists()) {
            ObjectMapper m = new ObjectMapper();
            JsonFactory factory = m.getFactory();
            try {
                settings = factory.createParser(f).readValueAs(Settings.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return settings;
    }

    public void save() {
        File f = new File(getFileName());
        ObjectMapper m = new ObjectMapper();
        JsonFactory factory = m.getFactory();
        try {
            factory.createGenerator(f, JsonEncoding.UTF8).writeObject(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @JsonIgnore
    public int getNofSources() {
        return sources.size();
    }

    public Source getSource(int idx) {
        return sources.get(idx);
    }

    public void setSource(int idx, Source source) {
        sources.set(idx, source);
    }

    public int getSelectedSource() {
        return selectedSource;
    }

    public void setSelectedSource(int selectedSource) {
        this.selectedSource = selectedSource;
    }

    private static String getFileName() {
        return System.getProperty("user.home") + File.separator + ".kidpython";
    }
}
