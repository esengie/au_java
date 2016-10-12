package ru.spbau.mit.Protocol.Requests;

import java.util.List;

public interface Request {
    String getName();
    List<String> getArgs();

    String toString();

    default byte[] toBytes() {
        return toString().getBytes();
    }
}
