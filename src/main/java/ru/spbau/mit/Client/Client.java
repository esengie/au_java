package ru.spbau.mit.Client;

public interface Client {
    void connect();
    void disconnect();
    void executeList();
    void executeGet();
}
