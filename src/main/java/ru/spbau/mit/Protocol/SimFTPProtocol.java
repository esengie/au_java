package ru.spbau.mit.Protocol;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public interface SimFTPProtocol {
    void formListRequest(String path, DataOutputStream output) throws IOException;
    void formGetRequest(String path, DataOutputStream output) throws IOException;
    List<RemoteFile> readListResponse(DataInputStream contents) throws IOException;
    void readGetResponse(DataInputStream contents, OutputStream out) throws IOException;

    void formResponse(DataInputStream request, DataOutputStream output) throws IOException;
}
