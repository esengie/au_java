package ru.spbau.mit.Communication;

import ru.spbau.mit.TorrentClient.TorrentFile.FileManager;
import ru.spbau.mit.TorrentClient.TorrentFile.TorrentFile;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.text.MessageFormat;
import java.util.Set;

public class TorrentProtocolSeedImpl implements TorrentProtocolSeed {
//    private File saveDir = new File(System.getenv("user.dir"))

//    public TorrentProtocolSeedImpl(File dir) throws ClientDirectoryException {
//        saveDir = dir;
//        if (!saveDir.exists() || !saveDir.isDirectory()) {
//            throw new ClientDirectoryException();
//        }
//    }

    @Override
    public void formResponse(DataInputStream in, DataOutputStream out, FileManager manager) throws IOException {
        int request = in.readInt();
        switch (request) {
            case 1:
                formStatResponse(in.readInt(), out, manager);
                return;
            case 2:
                formGetResponse(in.readInt(), in.readInt(), out, manager);
                return;
        }
        throw new BadInputException(MessageFormat.format("Unknown Command {0}", request));
    }

    /**
     * Parts are stored using the TorrentFile classes
     *
     * @param fileId - the id given by server
     * @param out - output stream
     * @throws IOException - if the stream provided throws the function throws
     */
    private void formStatResponse(int fileId, DataOutputStream out, FileManager manager) throws IOException {
        TorrentFile f = manager.getTorrentFile(fileId);
        if (f == null){
            out.writeInt(0);
            return;
        }
        Set<Integer> parts = f.getParts();
        for (int part : parts) {
            out.writeInt(part);
        }
    }

    private void formGetResponse(int fileId, int part, DataOutputStream out, FileManager manager) throws IOException {
        TorrentFile f = manager.getTorrentFile(fileId);
        if (f == null) {
            throw new ClientDirectoryException("File doesn't exist");
        }

        ByteBuffer buf = ByteBuffer.allocate(RemoteFile.PART_SIZE);
        f.read(buf, part);
        out.write(buf.array(), 0, buf.array().length);
    }
}
