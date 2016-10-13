package ru.spbau.mit.Server;


import ru.spbau.mit.Protocol.SimFTPProtocol;
import ru.spbau.mit.Protocol.SimFTPProtocolImpl;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerImpl implements Server {
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    private static void runInstance(int portNumber) {
        try (
                ServerSocket serverSocket = new ServerSocket(portNumber);
                Socket clientSocket = serverSocket.accept();
                DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());
                DataInputStream in = new DataInputStream(clientSocket.getInputStream());
        ) {
            SimFTPProtocol protocol = new SimFTPProtocolImpl();
            while (!Thread.interrupted()) {
                protocol.formResponse(in, out);
            }
        } catch (IOException e) {
            System.out.println("Exception caught when trying to listen on port "
                    + portNumber + " or listening for a connection");
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void start(int portNumber) {
        executor.execute(() -> {
            runInstance(portNumber);
        });
    }

    @Override
    public void stop() {
        executor.shutdownNow();
    }
}