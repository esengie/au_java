package ru.spbau.mit.TorrentClient;

import ru.spbau.mit.Protocol.RemoteFile;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.List;

public interface Client {
    void connect(String hostName) throws IOException;
    void disconnect() throws IOException;

    boolean isStopped();

    List<RemoteFile> executeList() throws IOException;
    RemoteFile executeUpload(File file) throws IOException;
    List<InetSocketAddress> executeSources(int fileId) throws IOException;
    void executeGet(File location, RemoteFile file) throws IOException;
}
