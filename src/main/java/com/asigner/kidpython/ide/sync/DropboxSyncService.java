package com.asigner.kidpython.ide.sync;

import com.asigner.kidpython.ide.PersistenceStrategy;

public class DropboxSyncService implements SyncService {

    @Override
    public void connect() {

    }

    @Override
    public String getName() {
        return "Dropbox";
    }

    @Override
    public boolean isConnected() {
        return false;
    }

    @Override
    public void disconnect() {

    }

    @Override
    public PersistenceStrategy getPersistenceStrategy() {
        return null;
    }
}
