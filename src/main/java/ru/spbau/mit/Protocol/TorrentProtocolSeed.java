package ru.spbau.mit.Protocol;

import java.io.IOException;

public interface TorrentProtocolSeed {
    void formResponse() throws IOException;
}
