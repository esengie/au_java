package ru.spbau.mit.TorrentClient.TorrentFile;


import ru.spbau.mit.Protocol.RemoteFile;

import java.io.File;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

public class TorrentFileLocal {

    private Set<Integer> parts = new ConcurrentSkipListSet<>();
    private FileChannel channel;
    public final int id;


    /**
     * Creates a remote file locally
     *
     * @param file - remote file, contains id, name and size
     */
    public TorrentFileLocal(RemoteFile file) {
        id = file.id;
    }

    /**
     * Loads a torrentfile from a file
     *
     * @param filepath -- path
     */
    TorrentFileLocal(File filepath){
        id = 0;
    }


    public Set<Integer> getParts() {
        return new HashSet<>(parts);
    }

    public void write(ByteBuffer b, int part) {

    }

    public int read(ByteBuffer b, int part) {
        return 0;
    }

    /**
     * Saves to disk
     */
    void close() {

    }
}
