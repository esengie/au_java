package ru.spbau.mit.Protocol.Responses;

public interface Response {
    String toString();

    default byte[] toBytes() {
        return toString().getBytes();
    }
}
