// Copyright 2016 Andreas Signer. All rights reserved.

package com.asigner.kidpython.ide.sync;

import com.asigner.kidpython.ide.sync.PersistenceStrategy;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class LocalPersistenceStrategy implements PersistenceStrategy {

    @Override
    public byte[] load() throws IOException {
        byte[] buf = new byte[10240];
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        FileInputStream fis = new FileInputStream(new File(getFileName()));
        int read;
        while ( (read = fis.read(buf)) > 0) {
            bos.write(buf, 0, read);
        }
        fis.close();
        bos.close();
        return bos.toByteArray();
    }

    @Override
    public void save(byte[] data) throws IOException {
        File f = new File(getFileName());
        FileOutputStream fos = new FileOutputStream(f);
        fos.write(data);
        fos.close();
    }

    private String getFileName() {
        return System.getProperty("user.home") + File.separator + ".programmablefun-coderepo.json";
    }

}
