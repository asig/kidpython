// Copyright 2016 Andreas Signer. All rights reserved.

package com.asigner.kidpython.ide;

import com.dropbox.core.DbxAppInfo;
import com.dropbox.core.DbxAuthFinish;
import com.dropbox.core.DbxDownloader;
import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.DbxWebAuthNoRedirect;
import com.dropbox.core.http.HttpRequestor;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.ListFolderResult;
import com.dropbox.core.v2.files.Metadata;
import com.dropbox.core.v2.files.UploadUploader;
import com.dropbox.core.v2.users.FullAccount;
;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Locale;

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
            UploadUploader uploader = client.files().upload(FILEPATH);
            OutputStream os = uploader.getOutputStream();
            os.write(data);
            uploader.finish();
            uploader.close();
        } catch (DbxException e) {
            throw new IOException(e);
        }
    }
}
