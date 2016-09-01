package com.asigner.kidpython.ide.sync;

import com.asigner.kidpython.ide.PersistenceStrategy;

public interface SyncService {
    String getName();
    boolean isConnected();
    void connect();
    void disconnect();
    PersistenceStrategy getPersistenceStrategy();

    static final SyncService[] ALL = new SyncService[] {
            new DropboxSyncService()
    };
}
