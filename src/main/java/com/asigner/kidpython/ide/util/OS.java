// Copyright 2016 Andreas Signer. All rights reserved.

package com.asigner.kidpython.ide.util;

public class OS {

    public static boolean isMac() {
        return System.getProperty( "os.name" ).equals( "Mac OS X" );
    }

    public static String getAppDataDirectory() {
        if (OS.isMac()) {
            return System.getProperty("user.home") + "/Library/Application Support";
        }

        String appdata = System.getenv("APPDATA");
        if (appdata != null) {
            // Windows
            return appdata;
        }

        // Linux
        appdata = System.getenv("XDG_DATA_HOME");
        if (appdata != null) {
            return appdata;
        } else {
            // Fall back to recommendation in https://standards.freedesktop.org/basedir-spec/basedir-spec-latest.html
            return System.getProperty("user.home") + "/.local/share";
        }
    }
}
