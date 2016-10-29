package ru.spbau.mit.Communication;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;

public interface TorrentProtocolServer {
    void formResponse(DataInputStream in, DataOutputStream out, InetSocketAddress client) throws IOException;

}
