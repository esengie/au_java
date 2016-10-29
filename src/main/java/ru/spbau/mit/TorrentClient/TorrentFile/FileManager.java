package ru.spbau.mit.TorrentClient.TorrentFile;

import ru.spbau.mit.Communication.RemoteFile;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class FileManager {
    // singleton
    Map<Integer, TorrentFile> files = new ConcurrentHashMap<>();

    public TorrentFile getTorrentFile(int fileId){
        return null;
    }

    public TorrentFile createTorrentFile(RemoteFile file){
        return null;
    }
}
