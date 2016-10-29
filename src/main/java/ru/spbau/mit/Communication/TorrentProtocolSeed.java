package ru.spbau.mit.Communication;

import ru.spbau.mit.TorrentClient.TorrentFile.FileManager;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public interface TorrentProtocolSeed {
    void formResponse(DataInputStream in, DataOutputStream out, FileManager manager) throws IOException;
}
