// Copyright 2016 Andreas Signer. All rights reserved.

package com.programmablefun;

public interface BuildInfo {
    Version getVersion();
    java.time.Instant getBuildTime();
    String getCommit();
    String getPlatform();
    String toString();
}
