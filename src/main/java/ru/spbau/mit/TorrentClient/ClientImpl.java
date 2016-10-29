package ru.spbau.mit.TorrentClient;

import ru.spbau.mit.Communication.RemoteFile;
import ru.spbau.mit.Communication.TorrentProtocolClient;
import ru.spbau.mit.Communication.TorrentProtocolClientImpl;

import java.io.*;
import java.net.Socket;
import java.util.List;

public class ClientImpl implements ru.spbau.mit.TorrentClient.Client {
    private boolean connected = false;
    Socket clientSocket;
    DataOutputStream netOut;
    DataInputStream netIn;
    TorrentProtocolClient protocol = new TorrentProtocolClientImpl();

    @Override
    public void connect(String hostName, int portNumber) throws IOException {
        if (connected)
            return;

        clientSocket = new Socket(hostName, portNumber);
        netOut = new DataOutputStream(clientSocket.getOutputStream());
        netIn = new DataInputStream(clientSocket.getInputStream());

        connected = true;
    }

    @Override
    public void disconnect() throws IOException {
        if (!connected)
            return;

        netIn.close();
        netOut.close();
        clientSocket.close();
    }

    @Override
    public List<RemoteFile> executeList(String path) throws IOException {
        if (!connected)
            return null;
        protocol.formListRequest(path, netOut);
        return protocol.readListResponse(netIn);
    }

    @Override
    public void executeGet(String path, OutputStream out) throws IOException {
        if (!connected)
            return;
        protocol.sendGetRequest(path, netOut);
        protocol.readGetResponse(netIn, out);
    }

}