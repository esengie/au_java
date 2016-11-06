package ru.spbau.mit.Protocol.Client;

import ru.spbau.mit.Protocol.Exceptions.BadInputException;
import ru.spbau.mit.Protocol.Exceptions.ClientDirectoryException;
import ru.spbau.mit.Protocol.RemoteFile;
import ru.spbau.mit.TorrentClient.TorrentFile.FileManager;
import ru.spbau.mit.TorrentClient.TorrentFile.TorrentFileLocal;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SeedProtocolImpl implements SeedProtocol {
    private static  final Logger logger = Logger.getLogger(SeedProtocol.class.getName());
    @Override
    public void formResponse(DataInputStream in, DataOutputStream out, FileManager manager) throws IOException {
        int request = in.readInt();
        switch (request) {
            case 1:
                logger.log(Level.FINE, "Serving stat request");
                formStatResponse(in.readInt(), out, manager);
                return;
            case 2:
                logger.log(Level.FINE, "Serving get request");
                formGetResponse(in.readInt(), in.readInt(), out, manager);
                return;
        }
        throw new BadInputException(MessageFormat.format("Unknown Command {0}", request));
    }

    /**
     * Parts are stored using the TorrentFileLocal classes
     *
     * @param fileId - the id given by server
     * @param out - output stream
     * @throws IOException - if the stream provided throws the function throws
     */
    private void formStatResponse(int fileId, DataOutputStream out, FileManager manager) throws IOException {
        TorrentFileLocal f = manager.getTorrentFile(fileId);
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
        TorrentFileLocal tFile = manager.getTorrentFile(fileId);
        if (tFile == null) {
            throw new ClientDirectoryException("File doesn't exist");
        }

        byte[] buf = new byte[RemoteFile.PART_SIZE];
        int size = tFile.read(buf, part);
        out.write(buf, 0, size);
    }
}
