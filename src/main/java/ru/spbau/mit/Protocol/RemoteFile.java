package ru.spbau.mit.Protocol;

public class RemoteFile {
    public final int id;
    public final String name;
    public final long size;

    public static final int PART_SIZE = 1 << 22; // 4 Mb

    public RemoteFile(int fileId, String fileName, long size){
        this.id = fileId;
        this.name = fileName;
        this.size = size;
    }

    public int parts() {
        return (int) (size + RemoteFile.PART_SIZE - 1) / RemoteFile.PART_SIZE;
    }
}
