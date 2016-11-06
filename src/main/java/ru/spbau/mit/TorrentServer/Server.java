package ru.spbau.mit.TorrentServer;

import java.io.File;
import java.io.IOException;

public interface Server {
    void start(File saveDir) throws IOException;
    void stop() throws TorrentIOException;
    boolean isStopped();
}
