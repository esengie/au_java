package ru.spbau.mit.Server;


import ru.spbau.mit.Protocol.SimFTPProtocol;
import ru.spbau.mit.Protocol.SimFTPProtocolImpl;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServerImpl implements Server {
    private static final Logger logger = Logger.getLogger(Server.class.getName());

    private final ExecutorService executor = Executors.newFixedThreadPool(2);
    private final SimFTPProtocol protocol = new SimFTPProtocolImpl();
    private volatile boolean isStopped = true;
    private ServerSocket serverSocket;

    private class FTPServerInstance implements Runnable {

        public void run() {
            try {
                while (!isStopped) {

                    Socket socket = serverSocket.accept();
                    executor.execute(() -> {
                        try {
                            DataInputStream in = new DataInputStream(socket.getInputStream());
                            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                            protocol.formResponse(in, out);
                            in.close();
                        } catch (IOException e) {
                            logger.log(Level.FINE, e.getMessage(), e);
                        }
                    });
                }
            } catch (SocketException e) {
                // server stop is working here
            } catch (IOException e) {
                logger.log(Level.WARNING, "Exception caught when trying to handle client", e);
            }
        }
    }

    @Override
    public void start(short portNumber) throws IOException {
        isStopped = false;
        serverSocket = new ServerSocket(portNumber);
        new Thread(new FTPServerInstance()).start();
    }

    @Override
    public void stop() throws IOException {
        isStopped = false;
        serverSocket.close();
        executor.shutdownNow();
    }
}