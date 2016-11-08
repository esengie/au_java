package ru.spbau.mit.Protocol;

import org.apache.commons.io.FileUtils;

import java.io.*;
import java.nio.file.Files;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

public class SimFTPProtocolImpl implements SimFTPProtocol {

    @Override
    public void formListRequest(String path, DataOutputStream output) throws IOException {
        output.writeInt(1);
        output.writeUTF(path);
    }

    @Override
    public void formGetRequest(String path, DataOutputStream output) throws IOException {
        output.writeInt(2);
        output.writeUTF(path);
    }

    @Override
    public List<RemoteFile> readListResponse(DataInputStream contents) throws IOException {
        int size = contents.readInt();
        List <RemoteFile> list = new ArrayList<>();
        for (int i = 0; i < size; ++i){
            list.add(new RemoteFile(contents.readUTF(), contents.readBoolean()));
        }
        return list;
    }

    /**
     * All this reading in a while loop because the length of contents may be huge
     * Due to size : long
     *
     * @param input input Socket
     * @param out output location
     * @throws IOException IO
     */
    @Override
    public void readGetResponse(DataInputStream input, OutputStream out) throws IOException {
        long size = input.readLong();
        byte[] buffer = new byte[1000000];
        int len;
        int bytesToRead = (long)buffer.length > size ? (int)size : buffer.length;
        while (bytesToRead > 0 && (len = input.read(buffer, 0, bytesToRead)) != -1) {
            out.write(buffer, 0, len);
            size -= (long) len;
            bytesToRead = (long) len > size ? (int)size : len;
        }
    }

    @Override
    public void formResponse(DataInputStream in, DataOutputStream out) throws IOException {
        int request = in.readInt();
        switch (request) {
            case 2:
                formGetResponse(in.readUTF(), out);
                return;
            case 1:
                formListResponse(in.readUTF(), out);
                return;
        }
        throw new BadInputException(MessageFormat.format("Unknown Command {0}", request));
    }

    private void formListResponse(String path, DataOutputStream out) throws IOException {
        File f = new File(path);
        if (!f.exists() || f.isFile()) {
            out.writeInt(0);
        }
        File[] dir = f.listFiles();
        out.writeInt(dir.length);
        for (File fin : dir){
            out.writeUTF(fin.getName());
            out.writeBoolean(fin.isDirectory());
        }
    }

    private void formGetResponse(String path, DataOutputStream out) throws IOException {
        File f = new File(path);
        if (!f.exists()){
            out.writeLong(0);
        }
        out.writeLong(f.length());
        Files.copy(f.toPath(), out);
    }
}
