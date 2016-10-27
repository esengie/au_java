package ru.spbau.mit.Protocol;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.MessageFormat;

public class TorrentProtocolSeedImpl implements TorrentProtocolSeed {
    private DataInputStream input;
    private DataOutputStream output;

    public TorrentProtocolSeedImpl(DataInputStream input, DataOutputStream output) {
        this.input = input;
        this.output = output;
    }

    @Override
    public void formResponse() throws IOException {
        int request = input.readInt();
        switch (request) {
            case 2:
                formGetResponse(input.readUTF(), output);
                return;
            case 11:
                formListResponse(input.readUTF(), output);
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
        for (File fin : dir) {
            out.writeUTF(fin.getAbsolutePath());
            out.writeBoolean(fin.isDirectory());
        }
    }

    private void formGetResponse(String path, DataOutputStream out) throws IOException {
        File f = new File(path);
        if (!f.exists()) {
            out.writeLong(0);
        }
        out.writeLong(f.length());
        Files.copy(f.toPath(), out);
    }
}
