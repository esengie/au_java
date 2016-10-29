package ru.spbau.mit.Communication;

public class RemoteFile {
    public final int id;
    public final String name;
    public final long size;

    public static final int PART_SIZE = 1 << 22; // 4 Mb

    RemoteFile(int fileId, String fileName, long size){
        this.id = fileId;
        this.name = fileName;
        this.size = size;
    }
}
