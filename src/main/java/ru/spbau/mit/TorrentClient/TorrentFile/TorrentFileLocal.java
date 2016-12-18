package ru.spbau.mit.TorrentClient.TorrentFile;


import ru.spbau.mit.Protocol.RemoteFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.FileAlreadyExistsException;
import java.util.HashSet;
import java.util.Observable;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TorrentFileLocal extends Observable {
    private static final Logger logger = Logger.getLogger(FileManager.class.getName());

    private final String mode = "rw";
    private final Set<Integer> parts = new ConcurrentSkipListSet<>();
    private final File localFile;
    private final long fileLength;

    /**
     * Creates a remote file locally
     *
     * @param file - remote file, contains id, name and size
     * @throws IOException -- if couldn't set the file length
     */
    TorrentFileLocal(File dir, RemoteFile file) throws IOException {
        localFile = new File(dir, file.name);
        if (!localFile.createNewFile()) {
            logger.log(Level.FINE, "Possibly file with the same name exists, couldn't create the file");
            throw new FileAlreadyExistsException("FileManager can't overwrite files");
        }
        fileLength = file.size;
        RandomAccessFile descriptor = new RandomAccessFile(localFile, mode);
        descriptor.setLength(file.size);
        descriptor.close();
    }

    /**
     * Loads a torrentfile from a file
     *
     * @param filepath -- path
     */
    TorrentFileLocal(File filepath, Set<Integer> parts) throws IOException {
        if (!filepath.exists() || filepath.isDirectory()) {
            logger.log(Level.SEVERE, "TorrentFile was passed a nonexistent file");
            throw new FileNotFoundException(filepath.getName());
        }
        this.parts.addAll(parts);
        localFile = filepath;
        try (RandomAccessFile descriptor = new RandomAccessFile(localFile, mode)) {
            fileLength = descriptor.length();
        }
    }

    /**
     * Creates a file from a local one
     *
     * @param filepath path to file
     */
    TorrentFileLocal(File filepath) throws IOException {
        int totalParts;
        try (RandomAccessFile descriptor = new RandomAccessFile(filepath, mode)) {
            fileLength = descriptor.length();
            totalParts = totalParts();
        } catch (IOException e) {
            logger.log(Level.SEVERE, "TorrentFile was passed a nonexistent file");
            throw e;
        }
        for (int i = 0; i < totalParts; ++i) {
            this.parts.add(i);
        }
        localFile = filepath;
    }

    public Set<Integer> getParts() {
        return new HashSet<>(parts);
    }

    /**
     * Could it be faster? Potentially, just curious.
     *
     * @param buf  buffer
     * @param part part number to read
     * @throws IOException if writes are throwing
     */
    public void write(byte[] buf, int part) throws IOException {
        try (RandomAccessFile descriptor = new RandomAccessFile(localFile, mode)) {
            descriptor.seek(part * RemoteFile.PART_SIZE);
            descriptor.write(buf, 0, partSize(part));
        }
        parts.add(part);
        setChanged();
        notifyObservers(percent());
    }

    public int totalParts() {
        return (int) (((fileLength - 1) + (long) RemoteFile.PART_SIZE) / (long) RemoteFile.PART_SIZE);
    }

    public int partSize(int part) throws IOException {
        if ((part + 1) * RemoteFile.PART_SIZE > fileLength) {
            return (int) (fileLength - part * RemoteFile.PART_SIZE);
        }
        return RemoteFile.PART_SIZE;
    }

    public int read(byte[] buf, int part) throws IOException {
        int bytesToRead = partSize(part);
        try (RandomAccessFile descriptor = new RandomAccessFile(localFile, mode)) {
            descriptor.seek(part * RemoteFile.PART_SIZE);
            descriptor.readFully(buf, 0, bytesToRead);
        }
        return bytesToRead;
    }

    public File getFile() {
        return localFile;
    }

    public double percent() {
        return parts.size() * 1.0 / totalParts();
    }
}
