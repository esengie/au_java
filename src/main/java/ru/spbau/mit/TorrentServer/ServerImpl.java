package ru.spbau.mit.TorrentServer;

import ru.spbau.mit.Protocol.ProtocolConstants;
import ru.spbau.mit.Protocol.Server.ServerProtocol;
import ru.spbau.mit.Protocol.Server.ServerProtocolImpl;
import ru.spbau.mit.Protocol.ServiceState;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class ServerImpl implements Server {

    private static final Logger logger = Logger.getLogger(ServerImpl.class.getName());

    public static final int PORT_NUMBER = 8081;
    private ServerSocket serverSocket = null;
    private volatile ServiceState serverState = ServiceState.PREINIT;
    private Thread serverThread = null;
    private Thread garbageCollectorThread = null;

    private ExecutorService threadPool = Executors.newFixedThreadPool(10);

    private ServerProtocol protocol;
    private Map<Socket, Timestamp> timeToLive = new ConcurrentHashMap<>();

    private class ServerThread implements Runnable {

        @Override
        public void run() {
            while (!isStopped()) {
                Socket clientSocket = null;
                try {
                    clientSocket = serverSocket.accept();
                    timeToLive.put(clientSocket, getNow());
                } catch (IOException e) {
                    if (isStopped()) {
                        break;
                    }
                    logger.log(Level.SEVERE, "Couldn't accept a client", e);
                }
                threadPool.execute(new WorkerRunnable(clientSocket));
            }
            threadPool.shutdown();
        }
    }

    private class GarbageCollectorThread implements Runnable {
        @Override
        public void run() {
            while (!isStopped()) {
                try {
                    Set<InetAddress> removed = timeToLive.entrySet()
                            .stream()
                            .filter(entry -> checkElapsed(entry.getValue()))
                            .map(entry -> entry.getKey().getInetAddress())
                            .collect(Collectors.toSet());

                    timeToLive.entrySet()
                            .removeIf(entry -> checkElapsed(entry.getValue()));

                    protocol.removeExtras(removed);
                    Thread.sleep(ProtocolConstants.TIMEOUT);
                } catch (InterruptedException e) {
                    logger.log(Level.WARNING, "Interrupted the garbage collector", e);
                }
            }
        }
    }


    public boolean isStopped() {
        return serverState == ServiceState.STOPPED;
    }

    @Override
    public void start(File saveDir) throws TorrentIOException {
        if (serverState != ServiceState.PREINIT)
            return;

        try {
            protocol = new ServerProtocolImpl(saveDir);
        } catch (IOException e) {
            throw new TorrentIOException("Couldn't load the state of the server", e);
        }
        openServerSocket();
        serverThread = new Thread(new ServerThread());
        serverThread.start();
        garbageCollectorThread = new Thread(new GarbageCollectorThread());
        garbageCollectorThread.start();
        serverState = ServiceState.RUNNING;
    }

    public synchronized void stop() throws TorrentIOException {
        if (isStopped())
            return;

        serverState = ServiceState.STOPPED;
        try {
            serverSocket.close();
            protocol.saveState();
        } catch (IOException e) {
            throw new TorrentIOException("Error closing server", e);
        }
    }

    private void openServerSocket() throws TorrentIOException {
        try {
            serverSocket = new ServerSocket(ServerImpl.PORT_NUMBER);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Cannot open port 8081", e);
            throw new TorrentIOException("Cannot open port 8081", e);
        }
    }

    private static Timestamp getNow() {
        return new Timestamp(Calendar.getInstance().getTimeInMillis());
    }

    private static boolean checkElapsed(Timestamp time) {
        return time.getTime() - getNow().getTime()
                > ProtocolConstants.TIMEOUT;
    }

    private class WorkerRunnable implements Runnable {
        Logger logger = Logger.getLogger(WorkerRunnable.class.getName());

        private Socket clientSocket;
        private DataOutputStream netOut;
        private DataInputStream netIn;

        WorkerRunnable(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        public void run() {
            try {
                netOut = new DataOutputStream(clientSocket.getOutputStream());
                netIn = new DataInputStream(clientSocket.getInputStream());
                protocol.formResponse(netIn, netOut, clientSocket.getInetAddress());
                netOut.close();
                netIn.close();
            } catch (IOException e) {
                logger.log(Level.SEVERE, "Client handler error", e);
            }
        }
    }
}



