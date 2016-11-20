package ru.spbau.mit.Protocol.Client;

import ru.spbau.mit.Protocol.RemoteFile;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

public class ClientProtocolImpl implements ClientProtocol {

    @Override
    public void sendListRequest(DataOutputStream output) throws IOException {
        output.writeByte(1);
    }

    @Override
    public List<RemoteFile> readListResponse(DataInputStream input) throws IOException {
        int size = input.readInt();
        List<RemoteFile> list = new ArrayList<>();
        for (int i = 0; i < size; ++i) {
            list.add(new RemoteFile(input.readInt(), input.readUTF(), input.readLong()));
        }
        return list;
    }

    @Override
    public void sendUploadRequest(DataOutputStream output, String name, long size) throws IOException {
        output.writeByte(2);
        output.writeUTF(name);
        output.writeLong(size);
    }

    @Override
    public int readUploadResponse(DataInputStream input) throws IOException {
        return input.readInt();
    }

    @Override
    public void sendSourcesRequest(DataOutputStream output, int fileId) throws IOException {
        output.writeByte(3);
        output.writeInt(fileId);
    }

    @Override
    public List<InetSocketAddress> readSourcesResponse(DataInputStream input) throws IOException {
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
    public void sendUpdateRequest(DataOutputStream output, short port, List<Integer> seedingFileIds) throws IOException {
        output.writeByte(4);
        output.writeShort(port);
        output.writeInt(seedingFileIds.size());
        for (int id : seedingFileIds) {
            output.writeInt(id);
        }
    }

    @Override
    public boolean readUpdateResponse(DataInputStream input) throws IOException {
        return input.readBoolean();
    }

    @Override
    public void sendStatRequest(DataOutputStream output, int fileId) throws IOException {
        output.writeByte(1);
        output.writeInt(fileId);
    }

    @Override
    public List<Integer> readStatResponse(DataInputStream input) throws IOException {
        int count = input.readInt();
        List<Integer> retVal = new ArrayList<>();
        for (int i = 0; i < count; ++i) {
            retVal.add(input.readInt());
        }
        return retVal;
    }

    @Override
    public void sendGetRequest(DataOutputStream output, int fileId, int part) throws IOException {
        output.writeByte(2);
        output.writeInt(fileId);
        output.writeInt(part);
    }

    @Override
    public void readGetResponse(DataInputStream input, byte[] buffer) throws IOException {
        // Reads up to that size
        input.readFully(buffer, 0, buffer.length);
    }
}
