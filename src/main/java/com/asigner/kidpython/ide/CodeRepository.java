package com.asigner.kidpython.ide;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class CodeRepository {

    private final PersistenceStrategy persistenceStrategy;

    private static class Content {
        @JsonProperty("selected_stylesheet")
        private int selectedStylesheet = 0;

        @JsonProperty("selected_source")
        private int selectedSource = 0;

        @JsonProperty("sources")
        private List<Source> sources;

        Content() {
            sources = Lists.newArrayList();
            for (int i = 0; i < SOURCES; i++) {
                sources.add(new Source(String.format("%d", i+1), ""));
            }
        }
    }

    private final static int SOURCES = 10;

    private Content content = new Content();

    public void load() {
        try {
            byte[] data = persistenceStrategy.load();
            ObjectMapper m = new ObjectMapper();
            JsonFactory factory = m.getFactory();
            content = factory.createParser(data).readValueAs(Content.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void save() {
        ObjectMapper m = new ObjectMapper();
        JsonFactory factory = m.getFactory();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            factory.createGenerator(bos, JsonEncoding.UTF8).writeObject(content);
            persistenceStrategy.save(bos.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @JsonIgnore
    public int getNofSources() {
        return SOURCES;
    }

    public Source getSource(int idx) {
        return content.sources.get(idx);
    }

    public void setSource(int idx, Source source) {
        content.sources.set(idx, source);
        save();
    }

    public int getSelectedSource() {
        return content.selectedSource;
    }

    public void setSelectedSource(int selectedSource) {
        content.selectedSource = selectedSource;
    }

    public int getSelectedStylesheet() {
        return content.selectedStylesheet;
    }

    public void setSelectedStylesheet(int selectedStylesheet) {
        content.selectedStylesheet = selectedStylesheet;
        save();
    }

    public CodeRepository(PersistenceStrategy persistenceStrategy) {
        this.persistenceStrategy = persistenceStrategy;
    }
}
