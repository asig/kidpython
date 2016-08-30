// Copyright 2016 Andreas Signer. All rights reserved.

package com.asigner.kidpython.ide;

import java.io.FileNotFoundException;
import java.io.IOException;

public interface PersistenceStrategy {
    byte[] load() throws IOException;
    void save(byte[] data) throws IOException;
}
