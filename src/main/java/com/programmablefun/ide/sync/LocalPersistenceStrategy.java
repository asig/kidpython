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

import com.programmablefun.ide.Settings;

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
        return Settings.getDataDirectory() + "/coderepo.json";
    }
}
