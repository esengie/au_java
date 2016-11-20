package ru.spbau.mit.Client;

import ru.spbau.mit.Protocol.RemoteFile;
import ru.spbau.mit.Protocol.SimFTPProtocol;
import ru.spbau.mit.Protocol.SimFTPProtocolImpl;

import java.io.*;
import java.net.Socket;
import java.util.List;

public class ClientImpl implements Client {
    private boolean connected = false;
    private Socket clientSocket;
    private DataOutputStream netOut;
    private DataInputStream netIn;
    private String host;
    private short port;

    private SimFTPProtocol protocol = new SimFTPProtocolImpl();

    @Override
    public void connect(String hostName, short portNumber) throws IOException {
        if (connected)
            return;
        host = hostName;
        port = portNumber;
        connected = true;
    }

    private void connect() throws IOException {
        clientSocket = new Socket(host, port);
        netOut = new DataOutputStream(clientSocket.getOutputStream());
        netIn = new DataInputStream(clientSocket.getInputStream());
    }

    @Override
    public void disconnect() throws IOException {
        if (!connected)
            return;
        connected = false;
        netIn.close();
    }

    @Override
    public List<RemoteFile> executeList(String path) throws IOException {
        if (!connected)
            return null;
        connect();
        protocol.formListRequest(path, netOut);
        List<RemoteFile> res = protocol.readListResponse(netIn);
        netIn.close();
        return res;
    }

    @Override
    public void executeGet(String path, OutputStream out) throws IOException {
        if (!connected)
            return;
        connect();
        protocol.formGetRequest(path, netOut);
        protocol.readGetResponse(netIn, out);
        disconnect();
    }

}