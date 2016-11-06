package ru.spbau.mit.TorrentClient.TorrentFile;


import ru.spbau.mit.Protocol.RemoteFile;
import ru.spbau.mit.TorrentServer.Server;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TorrentFileLocal {
    private static final Logger logger = Logger.getLogger(FileManager.class.getName());

    private String mode = "rwd";
    private Set<Integer> parts = new ConcurrentSkipListSet<>();
    private RandomAccessFile descriptor;
    private File localFile;

    /**
     * Creates a remote file locally
     *
     * @param file - remote file, contains id, name and size
     * @throws IOException -- if couldn't set the file length
     */
    TorrentFileLocal(File dir, RemoteFile file) throws IOException {
        localFile = new File(dir, file.name);
        if (!localFile.createNewFile()) {
            logger.log(Level.WARNING, "Possibly file with the same name exists, couldn't create the file");
            return;
        }
        descriptor = new RandomAccessFile(localFile, mode);
        descriptor.setLength(file.size);
    }

    /**
     * Loads a torrentfile from a file
     *
     * @param filepath -- path
     */
    TorrentFileLocal(File filepath, Set<Integer> parts) {
        try {
            descriptor = new RandomAccessFile(filepath, mode);
        } catch (FileNotFoundException e) {
            logger.log(Level.SEVERE, "TorrentFile was passed a nonexistent file");
        }
        this.parts.addAll(parts);
        localFile = filepath;
    }

    /**
     * Creates a file from a local one
     *
     * @param filepath path to file
     */
    TorrentFileLocal(File filepath) {

        int totalParts = 0;
        try {
            descriptor = new RandomAccessFile(filepath, mode);
            totalParts = totalParts(descriptor.length());
        } catch (IOException e) {
            logger.log(Level.SEVERE, "TorrentFile was passed a nonexistent file");
        }
        for (int i = 0; i < totalParts; ++i) {
            this.parts.add(i);
        }
        localFile = filepath;
    }

    public Set<Integer> getParts() {
        return new HashSet<>(parts);
    }

    public void write(byte[] buf, int part) throws IOException {
        descriptor.write(buf, part * RemoteFile.PART_SIZE, buf.length);
        parts.add(part);
    }

    public static int totalParts(long size){
        return (int)(((size - 1) + (long)RemoteFile.PART_SIZE) / (long)RemoteFile.PART_SIZE);
    }

    public int partSize(int part) throws IOException {
        long fileLength = descriptor.length();
        if ((part + 1) * RemoteFile.PART_SIZE > fileLength){
            return (int)(fileLength - part * RemoteFile.PART_SIZE);
        }
        return RemoteFile.PART_SIZE;
    }

    public int read(byte[] buf, int part) throws IOException {
        int bytesToRead = partSize(part);

        descriptor.readFully(buf, part * RemoteFile.PART_SIZE, bytesToRead);
        return bytesToRead;
    }

    /**
     * Saves to disk
     */
    synchronized void close() {
        try {
            descriptor.close();
        } catch (IOException e) {
            logger.log(Level.WARNING, "Descriptor error occured while closing");
        }
    }

    public File getFile() {
        return localFile;
    }
}
