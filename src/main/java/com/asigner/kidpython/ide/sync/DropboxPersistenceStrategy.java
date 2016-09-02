// Copyright 2016 Andreas Signer. All rights reserved.

package com.asigner.kidpython.ide.sync;

import com.asigner.kidpython.ide.sync.PersistenceStrategy;
import com.dropbox.core.DbxDownloader;
import com.dropbox.core.DbxException;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.UploadUploader;
import com.dropbox.core.v2.files.WriteMode;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class DropboxPersistenceStrategy implements PersistenceStrategy {

    private static final String FILEPATH = "/coderepo.json";

    private final DbxClientV2 client;

    public DropboxPersistenceStrategy(DbxClientV2 client) {
        this.client = client;
    }

    @Override
    public byte[] load() throws IOException {
        try {
            DbxDownloader downloader = client.files().download(FILEPATH);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            downloader.download(bos);
            downloader.close();
            return bos.toByteArray();
        } catch (DbxException e) {
            throw new IOException(e);
        }
    }

    @Override
    public void save(byte[] data) throws IOException {
        try {
            UploadUploader uploader = client.files()
                    .uploadBuilder(FILEPATH)
                    .withAutorename(false)
                    .withMode(WriteMode.OVERWRITE)
                    .start();
            OutputStream os = uploader.getOutputStream();
            os.write(data);
            uploader.finish();
            uploader.close();
        } catch (DbxException e) {
            throw new IOException(e);
        }
    }
}
