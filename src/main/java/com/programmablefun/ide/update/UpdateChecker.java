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

package com.programmablefun.ide.update;

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
        String platform = "";
        Properties props = System.getProperties();
        String osName = props.getProperty("os.name").toLowerCase();
        if (osName.startsWith("windows")) {
            platform = "windows";
        } else if (osName.startsWith("linux")) {
            platform = "linux";
        } else if (osName.startsWith("mac os x")) {
            platform = "macosx";
        } else {
            platform = "unknown";
        }
        String osArch = props.getProperty("os.arch");
        if (osArch.contains("64")) {
            platform += "_x64";
        } else if (osArch.contains("86")) {
            platform += "_x86";
        } else {
            platform += "_unknown";
        }
        return platform;
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
