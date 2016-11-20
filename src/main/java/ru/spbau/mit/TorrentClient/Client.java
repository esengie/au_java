package ru.spbau.mit.TorrentClient;

import ru.spbau.mit.Protocol.RemoteFile;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.List;

/**
 * Quite a heavy class - implements the peer and the seed functionality of a client.
 *
 * Peer: can poll the server using the ClientProtocol,
 * can poll the seed using SeedProtocol part of the ClientProtocol.
 *
 * Seed: updates the server each 5 minutes, can handle qet requests and stat requests
 * - return number of parts it has.
 *
 * The class serializes self (remembers the state of files downloaded)
 */
public interface Client {
    void connect(String hostName) throws IOException;
    void disconnect() throws IOException;

    boolean isStopped();

    List<RemoteFile> executeList() throws IOException;
    RemoteFile executeUpload(File file) throws IOException;
    List<InetSocketAddress> executeSources(int fileId) throws IOException;
    void executeGet(File location, RemoteFile file) throws IOException;
}
