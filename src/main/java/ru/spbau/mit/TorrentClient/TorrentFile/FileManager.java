package ru.spbau.mit.TorrentClient.TorrentFile;

import ru.spbau.mit.Protocol.Exceptions.ClientDirectoryException;
import ru.spbau.mit.Protocol.RemoteFile;

import java.io.File;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class FileManager {
    // singleton
    private Map<Integer, TorrentFileLocal> files = new ConcurrentHashMap<>();

    public FileManager(File saveDir) throws ClientDirectoryException {
        if (!saveDir.exists() || !saveDir.isDirectory()){
            throw new ClientDirectoryException("Directory doesn't exist or corrupted");
        }
        File[] dir = saveDir.listFiles();
        if (dir == null) return;

        for (File f : dir){
            TorrentFileLocal file = new TorrentFileLocal(f);
            files.put(file.id, file);
        }
    }

    public TorrentFileLocal getTorrentFile(int fileId){
        return files.getOrDefault(fileId, null);
    }

    public synchronized TorrentFileLocal createTorrentFile(RemoteFile file){
        TorrentFileLocal f = new TorrentFileLocal(file);
        files.put(file.id, f);
        return f;
    }

    public synchronized void saveToDisk(){

    }
}
