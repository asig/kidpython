// Copyright 2016 Andreas Signer. All rights reserved.

package com.asigner.kidpython.ide.util;

import java.io.File;

public class OS {

    public static boolean isMac() {
        return System.getProperty( "os.name" ).equals( "Mac OS X" );
    }

    public File getAppDataDirectory() {
        return new File(System.getProperty("user.home"));
    }
}
