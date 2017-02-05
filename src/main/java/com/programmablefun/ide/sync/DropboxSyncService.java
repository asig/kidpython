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
import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.ListFolderResult;
import com.dropbox.core.v2.files.Metadata;
import com.dropbox.core.v2.files.UploadUploader;
import com.dropbox.core.v2.users.FullAccount;
import org.eclipse.swt.SWT;
import org.eclipse.swt.program.Program;
import org.eclipse.swt.widgets.Display;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Optional;

public class DropboxSyncService implements SyncService {

    private static final String KEY_CONNECTED = "DropboxSyncService.connected";
    private static final String KEY_ACCESS_TOKEN = "DropboxSyncService.token";

    private Settings settings = Settings.getInstance();

    @Override
    public String getName() {
        return "Dropbox";
    }

    @Override
    public void connect() {
        settings.set(KEY_ACCESS_TOKEN, "");
        settings.set(KEY_CONNECTED, Boolean.toString(true));

        DropboxAutomatedAuthFlow flow = new DropboxAutomatedAuthFlow();
        Optional<String> token = flow.execute();
        if (token.isPresent()) {
            settings.set(KEY_ACCESS_TOKEN, token.get());
            settings.set(KEY_CONNECTED, Boolean.toString(true));
        } else {
            settings.set(KEY_CONNECTED, Boolean.toString(false));
        }
        settings.save();
    }

    @Override
    public boolean isConnected() {
        return settings.getBoolean(KEY_CONNECTED, false);
    }

    @Override
    public void disconnect() {
        settings.remove(KEY_ACCESS_TOKEN);
        settings.set(KEY_CONNECTED, Boolean.toString(false));
        settings.save();
    }

    @Override
    public PersistenceStrategy getPersistenceStrategy() {
        String token = settings.get(KEY_ACCESS_TOKEN, "");
        DbxRequestConfig config = DbxRequestConfig.newBuilder("ProgrammableFun/1.0").build();
        DbxClientV2 client = new DbxClientV2(config, token);
        return new DropboxPersistenceStrategy(client);
    }

    public static void main(String ... args) throws IOException, DbxException {

        DbxRequestConfig config = DbxRequestConfig.newBuilder("JavaTutorial/1.0").build();
//        DbxWebAuthNoRedirect webAuth = new DbxWebAuthNoRedirect(config, appInfo);
//        String authorizeUrl = webAuth.start();
//        System.out.println("1. Go to: " + authorizeUrl);
//        System.out.println("2. Click \"Allow\" (you might have to log in first)");
//        System.out.println("3. Copy the authorization code.");
//        String authCode = new BufferedReader(new InputStreamReader(System.in)).readLine().trim();
//        System.out.println("authCode = " + authCode);
//        DbxAuthFinish finish = webAuth.finish(authCode);
//        String token = finish.getAccessToken();
//        System.out.println("token = " + token);

        String token = "8YqEFOo6dWwAAAAAAAADj38QpG_9_Rdg94FOloyfaz9yhwdInsT5RRwF2qeV1Ax7";
        DbxClientV2 client = new DbxClientV2(config, token);

        FullAccount account = client.users().getCurrentAccount();
        System.out.println(account.getName().getDisplayName());

        ListFolderResult res = client.files().listFolder("");
        for (Metadata m : res.getEntries()) {
            System.out.println(m.getPathLower());
        }

        UploadUploader uploader = client.files().upload("/foo");
        OutputStream os = uploader.getOutputStream();
        os.write("Hello, World!\n".getBytes());
        uploader.finish();
        uploader.close();

    }

}
