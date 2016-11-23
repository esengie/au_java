package ru.spbau.mit.Protocol.Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.Set;

public interface ServerProtocol {
    int formResponse(DataInputStream in, DataOutputStream out, InetAddress client) throws IOException;

    void removeExtras(Set<InetSocketAddress> removed);
    void saveState() throws IOException;
}

