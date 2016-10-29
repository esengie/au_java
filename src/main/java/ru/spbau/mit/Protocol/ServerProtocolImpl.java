package ru.spbau.mit.Protocol;

import ru.spbau.mit.Protocol.Exceptions.BadInputException;
import ru.spbau.mit.Protocol.Exceptions.ServerDirectoryException;

import java.io.*;
import java.net.InetAddress;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

public class ServerProtocolImpl implements ServerProtocol {
    private Map<Integer, RemoteFile> idToFile = new ConcurrentHashMap<>();
    private Map<Integer, Set<InetAddress>> fileToSeedIPs = new ConcurrentHashMap<>();
    private Map<InetAddress, Integer> IPtoSeedPort = new ConcurrentHashMap<>();
    // Needs a way to disconnect guys
    private final Boolean writerLockIDToFile = false;
    private final Boolean writerLockIPs = false;
    private File saveDir;

    @Override
    public void formResponse(DataInputStream in, DataOutputStream out, InetAddress client) throws IOException {
        int request = in.readInt();
        switch (request) {
            case 1:
                formListResponse(out);
                return;
            case 2:
                formUploadResponse(in.readUTF(), in.readLong(), out);
                return;
            case 3:
                formSourcesResponse(in.readInt(), out);
                return;
            case 4:
                formUpdateResponse(in, out, client);
                return;
        }
        throw new BadInputException(MessageFormat.format("Unknown Command {0}", request));
    }

    @Override
    public void removeExtras(Set<InetAddress> removed) {
        synchronized (writerLockIPs) {
            IPtoSeedPort.keySet().removeIf(removed::contains);
            fileToSeedIPs.forEach((id, set) -> set.removeIf(removed::contains));
        }
    }

    private void formListResponse(DataOutputStream out) throws IOException {
        int count = idToFile.size();
        out.writeInt(count);
        for (int i = 0; i < count; ++i) {
            RemoteFile f = idToFile.get(i);
            out.writeInt(f.id);
            out.writeUTF(f.name);
            out.writeLong(f.size);
        }
    }

    private void formUploadResponse(String fileName, long size, DataOutputStream out) throws IOException {
        int id;
        synchronized (writerLockIDToFile) {
            id = idToFile.size();
            idToFile.put(id, new RemoteFile(id, fileName, size));
            fileToSeedIPs.put(id, new ConcurrentSkipListSet<>());
        }

        out.writeInt(id);
    }

    private void formSourcesResponse(int fileId, DataOutputStream out) throws IOException {
        if (!fileToSeedIPs.containsKey(fileId)) {
            out.writeInt(0);
            return;
        }

        // We care if the set gets updated during iteration
        // (main reason being the client expects the exact number of ips)
        // so we use "copy on write"
        Set<InetAddress> ips;
        Map<InetAddress, Integer> ipToPorts;

        synchronized (writerLockIPs) {
            ips = new HashSet<>(fileToSeedIPs.get(fileId));
            ipToPorts = new HashMap<>(IPtoSeedPort);
        }

        out.writeInt(ips.size());
        for (InetAddress ip : ips) {
            out.write(ip.getAddress(), 0, 4);
            out.writeShort(ipToPorts.get(ip));
        }
    }

    private void formUpdateResponse(DataInputStream in, DataOutputStream out, InetAddress ip) throws IOException {
        int port = in.readInt();
        int count = in.readInt();
        Set<Integer> fileIds = new HashSet<>();
        for (int i = 0; i < count; ++i) {
            fileIds.add(in.readInt());
        }
        synchronized (writerLockIPs) {
            IPtoSeedPort.put(ip, port);
            for (int fileId : fileIds) {
                if (!fileToSeedIPs.containsKey(fileId)) {
                    fileToSeedIPs.put(fileId, new ConcurrentSkipListSet<>());
                }
                fileToSeedIPs.get(fileId).add(ip);
            }
        }
        out.writeBoolean(true);
    }

    public ServerProtocolImpl(File saveDir) throws IOException {
        if (!saveDir.exists() || !saveDir.isDirectory())
            throw new ServerDirectoryException("Folder should exist");

        this.saveDir = saveDir;

        File outFile = new File(saveDir, idToFileSave);
        if (!outFile.exists())
            return;

        loadState(outFile);
    }

    private static final String idToFileSave = "idToFile.sav";

    public void saveState() throws IOException {
        File outFile = new File(saveDir, idToFileSave);
        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(outFile));
        out.writeObject(idToFile);
    }

    private void loadState(File outFile) throws IOException {
        ObjectInputStream out = new ObjectInputStream(new FileInputStream(outFile));
        try {
            idToFile = (Map<Integer, RemoteFile>) out.readObject();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Shouldn't happen here, serialization error");
        }
    }
}
