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

package com.programmablefun.ide.sync;

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
