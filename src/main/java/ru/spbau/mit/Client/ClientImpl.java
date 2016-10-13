package ru.spbau.mit.Client;

import ru.spbau.mit.Protocol.RemoteFile;
import ru.spbau.mit.Protocol.SimFTPProtocol;
import ru.spbau.mit.Protocol.SimFTPProtocolImpl;

import java.io.*;
import java.net.Socket;
import java.util.List;

public class ClientImpl implements Client {
    private boolean connected = false;
    Socket clientSocket;
    DataOutputStream netOut;
    DataInputStream netIn;
    SimFTPProtocol protocol = new SimFTPProtocolImpl();

    @Override
    public void connect(String hostName, int portNumber) throws IOException {
        if (connected)
            return;

        clientSocket = new Socket(hostName, portNumber);
        DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());
        DataInputStream in = new DataInputStream(clientSocket.getInputStream());

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
        protocol.formGetRequest(path, netOut);
        protocol.readGetResponse(netIn, out);
    }

}