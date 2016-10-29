package ru.spbau.mit.TorrentServer;

public interface Server {
    void start(int portNumber);
    void stop();
}
