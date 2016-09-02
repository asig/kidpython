package com.asigner.kidpython.ide.sync;

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
