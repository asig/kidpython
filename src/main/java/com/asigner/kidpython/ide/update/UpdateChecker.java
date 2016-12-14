package com.asigner.kidpython.ide.update;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;

public class UpdateChecker {

    /*
    "latest.json" looks like this:

     {
       "product": "ProductName",
       "version": "0.1.2",
       "release_date": "2016-01-02",
       "artifacts": {
         "linux_x86": [
           {
             "description": "Debian package for x86",
             "url": "http://example.com/debian.deb"
           },
           {
             "description": "Suse package for x86",
             "url": "http://example.com/debian.rpm"
           }
         ],
         "linux_x64": [
           {
             "description": "Debian package for x86",
             "url": "http://example.com/debian_64.deb"
           },
           {
             "description": "Suse package for x86",
             "url": "http://example.com/debian_64.rpm"
           }
         ],
         "windows_x86": [
           {
             "description": "Windows installer",
             "url": "http://example.com/installer.exe"
           }
         ],
         "macosx_x64": [
           {
             "description": "DMG image",
             "url": "http://example.com/image.dmg"
           }
         ]
       }
     }
     */

    @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE)
    public static class Artifact {
        @JsonProperty("description")
        private String description;

        @JsonProperty("url")
        private URL url;
    }

    @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE)
    public static class Descriptor {
        @JsonProperty("product")
        private String product;

        @JsonProperty("version")
        private String version;

        @JsonProperty("release_date")
        private String releaseDate;

        @JsonProperty("artifacts")
        private Map<String, List<Artifact>> artifacts;
    }


    private final URL url;

    public UpdateChecker(URL url) {
        this.url = url;
    }

    Optional<Descriptor> checkForUpdates() {
        String platform = buildPlatform();
        return null;

    }

    private String buildPlatform() {
        Properties props = System.getProperties();
        String osName = props.getProperty("os.name");
        String osArch = props.getProperty("os.arch");

    }


    public static void main(String ... args) {
        String json = "     {\n" +
                "       \"product\": \"ProductName\",\n" +
                "       \"version\": \"0.1.2\",\n" +
                "       \"release_date\": \"2016-01-02\",\n" +
                "       \"artifacts\": {\n" +
                "         \"linux_x86\": [\n" +
                "           {\n" +
                "             \"description\": \"Debian package for x86\",\n" +
                "             \"url\": \"http://example.com/debian.deb\"\n" +
                "           },\n" +
                "           {\n" +
                "             \"description\": \"Suse package for x86\",\n" +
                "             \"url\": \"http://example.com/debian.rpm\"\n" +
                "           }\n" +
                "         ],\n" +
                "         \"linux_x64\": [\n" +
                "           {\n" +
                "             \"description\": \"Debian package for x86\",\n" +
                "             \"url\": \"http://example.com/debian_64.deb\"\n" +
                "           },\n" +
                "           {\n" +
                "             \"description\": \"Suse package for x86\",\n" +
                "             \"url\": \"http://example.com/debian_64.rpm\"\n" +
                "           }\n" +
                "         ],\n" +
                "         \"windows_x86\": [\n" +
                "           {\n" +
                "             \"description\": \"Windows installer\",\n" +
                "             \"url\": \"http://example.com/installer.exe\"\n" +
                "           }\n" +
                "         ],\n" +
                "         \"macosx_x64\": [\n" +
                "           {\n" +
                "             \"description\": \"DMG image\",\n" +
                "             \"url\": \"http://example.com/image.dmg\"\n" +
                "           }\n" +
                "         ]\n" +
                "       }\n" +
                "     }\n";
        ObjectMapper mapper = new ObjectMapper();
        Descriptor desc = null;
        try {
            desc = mapper.readerFor(Descriptor.class).readValue(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.err.println(desc);
    }
}
