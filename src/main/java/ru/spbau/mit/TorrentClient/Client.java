package ru.spbau.mit.TorrentClient;

import ru.spbau.mit.Protocol.RemoteFile;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public interface Client {
    void connect(String hostName, int port) throws IOException;
    void disconnect() throws IOException;

    List<RemoteFile> executeList() throws IOException;
    void executeGet(RemoteFile file, OutputStream out) throws IOException;
}
