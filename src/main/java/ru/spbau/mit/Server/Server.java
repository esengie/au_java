package ru.spbau.mit.Server;

import java.io.IOException;

public interface Server {
    void start(short portNumber) throws IOException;
    void stop() throws IOException;
}
