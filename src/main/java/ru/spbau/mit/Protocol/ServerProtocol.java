package ru.spbau.mit.Protocol;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.util.Set;

public interface ServerProtocol {
    void formResponse(DataInputStream in, DataOutputStream out, InetAddress client) throws IOException;

    void removeExtras(Set<InetAddress> removed);
    void saveState() throws IOException;
}

