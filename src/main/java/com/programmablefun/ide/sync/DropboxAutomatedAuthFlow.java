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

import com.dropbox.core.DbxAppInfo;
import com.dropbox.core.DbxAuthFinish;
import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.DbxSessionStore;
import com.dropbox.core.DbxWebAuth;
import com.google.common.collect.Maps;
import com.sun.net.httpserver.HttpServer;
import org.eclipse.swt.program.Program;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.util.Map;
import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class DropboxAutomatedAuthFlow {

    private static class SessionStore implements DbxSessionStore {
        private String val = null;

        @Override
        public void clear() {
            val = null;
        }

        @Override
        public String get() {
            return val;
        }

        @Override
        public void set(String value) {
            val = value;
        }
    }

    private static final String APP_KEY = "wgcfdvmpgau5h5m";
    private static final String APP_SECRET = "qn9g8ytz4f4ko47";

    private static final int PORT = 22231;

    private HttpServer server;
    private CountDownLatch tokenReceivedSignal;
    private volatile String authToken;
    private DbxWebAuth webAuth;
    private SessionStore sessionStore;

    public String getCallbackUrl() {
        return String.format("http://localhost:%d/", PORT);
    }

    private void startServer() {
        try {
            // Start the server
            server = HttpServer.create(new InetSocketAddress(PORT), 0);
            server.createContext("/", httpExchange -> {
                String uri = httpExchange.getRequestURI().toString();
                Map<String, String[]> params = Maps.newHashMap();
                String[] paramStrs = uri.substring(uri.indexOf("?")+1).split("&");
                for (String paramStr : paramStrs) {
                    String[] parts = paramStr.split("=");
                    params.put(parts[0].trim(), new String[] { URLDecoder.decode(parts[1].trim(), "utf-8") });
                }
                String response = "You can close this tab now.";
                httpExchange.sendResponseHeaders(200, response.length());
                OutputStream os = httpExchange.getResponseBody();
                os.write(response.getBytes());
                os.close();

                finishFromRedirect(params);

                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        server.stop(0);
                    }
                }, 1000);
            });
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void finishFromRedirect(Map<String, String[]> params) {
        DbxAuthFinish finish = null;
        try {
            finish = webAuth.finishFromRedirect(getCallbackUrl(), sessionStore, params);
            authToken = finish.getAccessToken();
        } catch (DbxException | DbxWebAuth.BadRequestException | DbxWebAuth.NotApprovedException | DbxWebAuth.ProviderException | DbxWebAuth.CsrfException | DbxWebAuth.BadStateException e) {
            // TODO(asigner): Show error message
            e.printStackTrace();
            authToken = null;
        } finally {
            tokenReceivedSignal.countDown();;
        }
    }


    public Optional<String> execute() {
        DbxAppInfo appInfo = new DbxAppInfo(APP_KEY, APP_SECRET);
        DbxRequestConfig config = DbxRequestConfig.newBuilder("ProgrammableFun/1.0").build();
        tokenReceivedSignal = new CountDownLatch(1);

        String redirectUri = getCallbackUrl();
        sessionStore = new SessionStore();
        webAuth = new DbxWebAuth(config, appInfo);
        startServer();

        String authorizeUrl = webAuth.authorize(DbxWebAuth.newRequestBuilder().withRedirectUri(redirectUri, sessionStore).build());
        Program.launch(authorizeUrl);

        try {
            if (tokenReceivedSignal.await(5, TimeUnit.MINUTES)) {
                return Optional.ofNullable(authToken);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            server.stop(0);
        }
        return Optional.empty();
    }

}
