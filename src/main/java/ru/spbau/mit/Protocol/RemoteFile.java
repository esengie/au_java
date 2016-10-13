package ru.spbau.mit.Protocol;

public class RemoteFile {
    public final String path;
    public final boolean isDir;

    RemoteFile(String path, boolean isDir){
        this.path = path;
        this.isDir = isDir;
    }
}
