// Copyright 2016 Andreas Signer. All rights reserved.

package com.asigner.kidpython.ide.util;

import java.io.File;

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

        appdata = System.getenv("XDG_DATA_HOME");
        if (appdata != null) {
            return appdata;
        }

        // Neither win nor linux. Fall back to user.home
        return System.getProperty("user.home");
    }
}
