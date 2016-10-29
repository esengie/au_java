package ru.spbau.mit.Communication;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

public class TorrentProtocolServerImpl implements TorrentProtocolServer {
    private Map<Integer, RemoteFile> idToFile = new ConcurrentHashMap<>();
    private Map<Integer, Set<InetAddress>> fileToSeedIPs = new ConcurrentHashMap<>();
    private Map<InetAddress, Integer> IPtoSeedPort = new ConcurrentHashMap<>();
    // Needs a way to disconnect guys
    private final Boolean writerLockIDToFile = false;
    private final Boolean writerLockIPs = false;

    @Override
    public void formResponse(DataInputStream in, DataOutputStream out, InetSocketAddress client) throws IOException {
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
        if (!fileToSeedIPs.containsKey(fileId)){
            out.writeInt(0);
            return;
        }

        // We care if the set gets updated during iteration
        // (main reason being the client expects the exact number of ips)
        // so we use "copy on write"
        Set<InetAddress> ips;
        Map<InetAddress, Integer> ipToPorts;

        synchronized (writerLockIPs){
            ips = new HashSet<>(fileToSeedIPs.get(fileId));
            ipToPorts = new HashMap<>(IPtoSeedPort);
        }

        out.writeInt(ips.size());
        for (InetAddress ip : ips){
            out.write(ip.getAddress(), 0, 4);
            out.writeShort(ipToPorts.get(ip));
        }

    }

    private void formUpdateResponse(DataInputStream in, DataOutputStream out, InetSocketAddress client) throws IOException {
        int port = in.readInt();
        int count = in.readInt();
        InetAddress ip = client.getAddress();
        IPtoSeedPort.put(ip, port);

        for (int i = 0; i < count; ++i){
            int fileId = in.readInt();
            if (!fileToSeedIPs.get(fileId).contains(ip)) {
                fileToSeedIPs.get(fileId).add(ip);
            }
        }

        out.writeBoolean(true);
    }
}
