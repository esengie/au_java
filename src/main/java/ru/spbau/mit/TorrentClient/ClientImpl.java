package ru.spbau.mit.TorrentClient;

import ru.spbau.mit.Protocol.Exceptions.ClientDirectoryException;
import ru.spbau.mit.Protocol.RemoteFile;
import ru.spbau.mit.TorrentClient.TorrentFile.FileManager;

import java.io.*;
import java.net.Socket;
import java.util.List;

public class ClientImpl implements Client {
    private volatile boolean isStopped = false;
    private FileManager fileManager;

    private Leech leech;
    private Seed seed;

    public ClientImpl(File saveDir) throws ClientDirectoryException {
        fileManager = new FileManager(saveDir);
    }

    @Override
    public void connect(String hostName, int portNumber) throws IOException {
        if (isStopped)
            return;

        clientSocket = new Socket(hostName, portNumber);
        serverNetOut = new DataOutputStream(clientSocket.getOutputStream());
        serverNetIn = new DataInputStream(clientSocket.getInputStream());

        // should launch a seed guy

        isStopped = true;
    }

    @Override
    public void disconnect() throws IOException {
        if (!isStopped)
            return;

        seed.stop();
        leech.stop();
    }

    @Override
    public List<RemoteFile> executeList() throws IOException {
        if (!isStopped)
            return null;
        protocol.sendListRequest();
        return protocol.readListResponse();
    }

    @Override
    public void executeGet(RemoteFile file, OutputStream out) throws IOException {
        if (!isStopped)
            return;
        client.executeGet();
        // implement
    }

}