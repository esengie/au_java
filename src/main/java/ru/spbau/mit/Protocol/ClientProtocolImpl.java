package ru.spbau.mit.Protocol;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

public class ClientProtocolImpl implements ClientProtocol {
    private DataInputStream input;
    private DataOutputStream output;

    public ClientProtocolImpl(DataInputStream input, DataOutputStream output) {
        this.input = input;
        this.output = output;
    }

    @Override
    public void sendListRequest() throws IOException {
        output.writeByte(1);
    }

    @Override
    public List<RemoteFile> readListResponse() throws IOException {
        int size = input.readInt();
        List<RemoteFile> list = new ArrayList<>();
        for (int i = 0; i < size; ++i) {
            list.add(new RemoteFile(input.readInt(), input.readUTF(), input.readLong()));
        }
        return list;
    }

    @Override
    public void sendUploadRequest(String name, long size) throws IOException {
        output.writeByte(2);
        output.writeUTF(name);
        output.writeLong(size);
    }

    @Override
    public int readUploadResponse() throws IOException {
        return input.readInt();
    }

    @Override
    public void sendSourcesRequest(int fileId) throws IOException {
        output.writeByte(3);
        output.writeInt(fileId);
    }

    @Override
    public List<InetSocketAddress> readSourcesResponse() throws IOException {
        int size = input.readInt();
        List<InetSocketAddress> retVal = new ArrayList<>();
        for (int i = 0; i < size; ++i) {
            byte[] address = new byte[4];
            for (int k = 0; k < 4; ++k) {
                address[k] = input.readByte();
            }
            retVal.add(new InetSocketAddress(
                    InetAddress.getByAddress(address), input.readShort()));
        }
        return retVal;
    }

    @Override
    public void sendUpdateRequest(short port, List<Integer> seedingFileIds) throws IOException {
        output.writeByte(4);
        output.writeShort(port);
        output.writeInt(seedingFileIds.size());
        for (int id : seedingFileIds) {
            output.writeInt(id);
        }
    }

    @Override
    public boolean readUpdateResponse() throws IOException {
        return input.readBoolean();
    }

    @Override
    public void sendStatRequest(int fileId) throws IOException {
        output.writeByte(1);
        output.writeInt(fileId);
    }

    @Override
    public List<Integer> readStatResponse() throws IOException {
        int count = input.readInt();
        List<Integer> retVal = new ArrayList<>();
        for (int i = 0; i < count; ++i){
            retVal.add(input.readInt());
        }
        return retVal;
    }

    @Override
    public void sendGetRequest(int fileId, int part) throws IOException {
        output.writeByte(2);
        output.writeInt(fileId);
        output.writeInt(part);
    }

    @Override
    public void readGetResponse(OutputStream output) throws IOException {
        byte[] buffer = new byte[RemoteFile.PART_SIZE];
        // Reads up to that size
        int read = input.read(buffer, 0, RemoteFile.PART_SIZE);
        output.write(buffer, 0, read);
    }
}
