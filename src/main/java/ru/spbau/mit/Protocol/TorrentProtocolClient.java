package ru.spbau.mit.Protocol;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.List;

public interface TorrentProtocolClient {
    void sendListRequest() throws IOException;
    List<RemoteFile> readListResponse() throws IOException;

    void sendUploadRequest(String name, long size) throws IOException;
    int readUploadResponse() throws IOException;

    void sendSourcesRequest(int fileId) throws IOException;
    List<InetSocketAddress> readSourcesResponse() throws IOException;

    void sendUpdateRequest(short port, List<Integer> seedingFileIds) throws IOException;
    boolean readUpdateResponse() throws IOException;

    void sendStatRequest(int fileId) throws IOException;
    List<Integer> readStatResponse() throws IOException;

    void sendGetRequest(int fileId, int part) throws IOException;
    void readGetResponse(OutputStream output) throws IOException;

}
