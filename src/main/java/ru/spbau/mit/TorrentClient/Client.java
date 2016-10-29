package ru.spbau.mit.TorrentClient;

import ru.spbau.mit.Communication.RemoteFile;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public interface Client {
    void connect(String hostName, int port) throws IOException;
    void disconnect() throws IOException;
    List<RemoteFile> executeList(String path) throws IOException;
    void executeGet(String path, OutputStream out) throws IOException;
}
