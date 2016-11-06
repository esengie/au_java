package ru.spbau.mit.TorrentClient;

import ru.spbau.mit.Protocol.Client.ClientProtocol;
import ru.spbau.mit.Protocol.Client.ClientProtocolImpl;
import ru.spbau.mit.Protocol.ProtocolConstants;
import ru.spbau.mit.Protocol.RemoteFile;
import ru.spbau.mit.Protocol.ServiceState;
import ru.spbau.mit.TorrentClient.TorrentFile.FileManager;
import ru.spbau.mit.TorrentClient.TorrentFile.TorrentFileLocal;
import ru.spbau.mit.TorrentServer.ServerImpl;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientImpl implements Client {
    private static final Logger logger = Logger.getLogger(ClientImpl.class.getName());

    private volatile ServiceState clientState = ServiceState.PREINIT;
    private Socket socketToServer = null;
    public static final int NUM_THREADS = 10;

    private FileManager fileManager = null;
    private ClientProtocol protocol = new ClientProtocolImpl();
    private DataOutputStream netOut = null;
    private DataInputStream netIn = null;

    // files and parts mapped to seeds
    private Map<Integer, List<InetSocketAddress>> filesToSeeds = new ConcurrentHashMap<>();
    // FileIds we have to get
    // get, start on part and put back
    private BlockingQueue<FileToLeech> leechQueue =
            new LinkedBlockingQueue<>();

    private ExecutorService leeches = Executors.newFixedThreadPool(NUM_THREADS);

    private Thread keepAliveThread = null;
    private Seed seed = null;
    private String host;
    private short hostPort;
    private short seedPort;


    public ClientImpl(FileManager fm, short port) throws IOException {
        seedPort = port;
        fileManager = fm;
        seed = new Seed(port, fm);
    }

    public boolean isStopped() {
        return clientState == ServiceState.STOPPED;
    }

    private class KeepAliveThread implements Runnable {
        private DataOutputStream aliveOut;
        private DataInputStream aliveIn;
        private Socket socket;
        @Override
        public void run() {
            try {
                while (!isStopped()) {
                    socket = new Socket(host, hostPort);
                    aliveOut = new DataOutputStream(socket.getOutputStream());
                    aliveIn = new DataInputStream(socket.getInputStream());

                    protocol.sendUpdateRequest(aliveOut, seedPort, fileManager.getFileIds());
                    protocol.readUpdateResponse(aliveIn);

                    aliveIn.close();
                    Thread.sleep(Math.abs(ProtocolConstants.TIMEOUT - 1000));
                }
            } catch (InterruptedException | IOException e) {
                // do nothing?
            }
        }
    }

    /**
     * The algorithm is rather convoluted, but I think it works ok.
     * <p>
     * For each file we want to load we put
     * a FileToLeech structure n times into the queue
     * (where n is the number of parts left to download)
     * <p>
     * We also maintain a queue of seeds + a hashmap from seeds to the sets of parts they have
     * (if the seed is dead we remove it from the hashmap)
     * <p>
     * So each time we hit our FileToLeech in the leechQueue we poll the queue of seeds,
     * Stat the seed we get and try to get the part, that we don't yet have, off that seed
     * If we can't do it we put the FileToLeech back into the queue.
     * <p>
     * When the queue of seeds is empty we do sources again, remove the dead seeds
     * from the map, add the sources into the queue, first the new seeds then our old ones
     * and continue our stat, request cycle
     * <p>
     * ps so the seeds are under quite a light load, but we always perform an extra request
     * maybe I should improve this(?).
     */
    private class WorkerRunnable implements Runnable {
        private DataInputStream workerIn;
        private DataOutputStream workerOut;
        private Socket leechSocket;

        private void openLeechSocket(InetSocketAddress address) throws IOException {
            leechSocket = new Socket(address.getHostName(), address.getPort());
            workerOut = new DataOutputStream(leechSocket.getOutputStream());
            workerIn = new DataInputStream(leechSocket.getInputStream());
        }

        /**
         * Taking out a seed at random and get his parts whenever he has anything to offer
         */
        public void run() {
            while (!isStopped()) {
                try {
                    FileToLeech fp = leechQueue.poll(1, TimeUnit.SECONDS);
                    if (fp == null) {
                        Thread.sleep(2000);
                        continue;
                    }
                    // We get a seed from seed queue, it's empty in the beginning
                    InetSocketAddress seed = fp.seeds.poll();
                    Integer part = null;
                    try {
                        // Round robin we went let's stat again!
                        if (seed == null) {
                            reloadSources(fp);
                            continue;
                        }

                        openLeechSocket(seed);
                        protocol.sendStatRequest(workerOut, fp.fileId);
                        List<Integer> parts = protocol.readStatResponse(workerIn);
                        fp.seedParts.put(seed, new ConcurrentSkipListSet<>(parts));
                        workerIn.close();

                        part = getPart(fp, seed);
                        // If the seed can't seed this right now
                        if (part == null) {
                            leechQueue.add(fp);
                            continue;
                        }

                        openLeechSocket(seed);
                        protocol.sendGetRequest(workerOut, fp.fileId, part);
                        byte[] buffer = new byte[fp.file.partSize(part)];
                        protocol.readGetResponse(workerIn, buffer);
                        workerIn.close();

                    } catch (IOException e) {
                        // We are just overly cautious here
                        if (seed != null) {
                            fp.seedParts.remove(seed);
                        }
                        if (part != null){
                            fp.partsNeeded.add(part);
                        }
                        leechQueue.add(fp);

                        logger.log(Level.FINE, "Seed dead againg", e);
                    }
                } catch (InterruptedException e) {
                    logger.log(Level.WARNING, "Was interrupted", e);
                }
            }
        }

        private Integer getPart(FileToLeech fp, InetSocketAddress seed) {
            synchronized (fp.partsNeeded) {
                Set<Integer> pts = fp.seedParts.get(seed);
                for (int pt : fp.partsNeeded) {
                    if (pts.contains(pt)) {
                        pts.remove(pt);
                        return pt;
                    }
                }
                return null;
            }
        }

        private void reloadSources(FileToLeech fp) throws IOException {
            fp.seeds.addAll(executeSources(fp.fileId));
        }

    }

    @Override
    public void connect(String hostName) throws IOException {
        if (clientState != ServiceState.PREINIT)
            return;
        host = hostName;
        hostPort = ServerImpl.PORT_NUMBER;
        clientState = ServiceState.RUNNING;
        // should launch a seed guy
        keepAliveThread = new Thread(new KeepAliveThread());
        keepAliveThread.start();
        seed.start();

        for (int i = 0; i < NUM_THREADS; ++i) {
            leeches.submit(new WorkerRunnable());
        }
    }

    private void openClientSocket() throws IOException {
        socketToServer = new Socket(host, hostPort);
        netOut = new DataOutputStream(socketToServer.getOutputStream());
        netIn = new DataInputStream(socketToServer.getInputStream());
    }

    @Override
    public void disconnect() throws IOException {
        if (isStopped())
            return;
        clientState = ServiceState.STOPPED;
        seed.stop();
        try {
            leeches.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            logger.log(Level.SEVERE, "Interrupted disconnect", e);
        }
    }

    @Override
    public List<RemoteFile> executeList() throws IOException {
        if (isStopped())
            return null;
        openClientSocket();
        protocol.sendListRequest(netOut);
        return protocol.readListResponse(netIn);
    }

    @Override
    public RemoteFile executeUpload(File file) throws IOException {
        if (isStopped())
            return null;
        openClientSocket();
        protocol.sendUploadRequest(netOut, file.getName(), file.length());
        RemoteFile f = new RemoteFile(protocol.readUploadResponse(netIn),
                file.getName(), file.length());
        fileManager.addTorrentFile(file, f);
        return f;
    }

    @Override
    public List<InetSocketAddress> executeSources(int fileId) throws IOException {
        if (isStopped())
            return null;
        openClientSocket();
        protocol.sendSourcesRequest(netOut, fileId);
        filesToSeeds.put(fileId, protocol.readSourcesResponse(netIn));
        return filesToSeeds.get(fileId);
    }

    /**
     * Since it's not in the spec I don't continue loading incomplete files on restart
     * Only when asked
     *
     * @param location where we store the file
     * @param file     the remote file to get
     * @throws IOException -- could throw on a network error, or file creation error
     */
    @Override
    public void executeGet(File location, RemoteFile file) throws IOException {
        if (isStopped())
            return;
        TorrentFileLocal f = fileManager.getTorrentFile(file.id);
        if (f == null) {
            fileManager.createTorrentFile(location, file);
            f = fileManager.getTorrentFile(file.id);
        }

        if (!f.getFile().getParentFile().equals(location)) {
            throw new TorrentExistsException("The file" + f.getFile().getName() +
                    "is already downloading into another location: " +
                    f.getFile().getParentFile().getAbsolutePath());
        }

        Set<Integer> partsDone = new HashSet<>(f.getParts());

        if (partsDone.size() == file.parts())
            return;

        Set<Integer> partsNeeded = new HashSet<>();
        for (int i = 0; i < file.parts(); ++i) {
            if (!partsDone.contains(i)) {
                partsNeeded.add(i);
            }
        }

        partsNeeded = new ConcurrentSkipListSet<>(partsNeeded);
        FileToLeech fl = new FileToLeech(file.id, partsNeeded, f);
        executeSources(file.id);
        filesToSeeds.get(file.id).forEach(fl.seeds::add);
        for (int i = 0; i < file.parts(); ++i) {
            if (!partsDone.contains(i)) {
                leechQueue.add(fl);
            }
        }
    }
}