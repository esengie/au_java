package ru.spbau.mit.TorrentClient.TorrentFile;


import ru.spbau.mit.Communication.RemoteFile;

import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

public class TorrentFile {

    private Set<Integer> parts = new ConcurrentSkipListSet<>();
    private FileChannel channel;

    public TorrentFile(RemoteFile f) {
//        if exists load
//                else create.

    }

    public Set<Integer> getParts() {
        return new HashSet<>(parts);
    }

    public void write(ByteBuffer b, int part) {

    }

    public void read(ByteBuffer b, int part) {

    }

}
