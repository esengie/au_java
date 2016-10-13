package ru.spbau.mit.Protocol;

import ru.spbau.mit.Protocol.Responses.Response;

import java.io.IOException;

// Client and server parts?
public interface SimFTPProtocol {
    byte[] formListRequest(String path);
    byte[] formGetRequest(String path);
    Response readResponse(byte[] contents);

    Response formResponse(byte[] request) throws IOException;
}
