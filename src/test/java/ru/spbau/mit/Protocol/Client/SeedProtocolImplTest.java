package ru.spbau.mit.Protocol.Client;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import ru.spbau.mit.Common.WithFileManager;
import ru.spbau.mit.Protocol.RemoteFile;
import ru.spbau.mit.Protocol.Server.ServerProtocolImpl;
import ru.spbau.mit.TorrentClient.TorrentFile.FileManager;

import java.io.*;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class SeedProtocolImplTest {
    private ByteArrayOutputStream outContent;
    private ByteArrayOutputStream inContent;
    private DataInputStream outIn;
    private FileManager fm;

    private final ClientProtocol client = new ClientProtocolImpl();
    private final SeedProtocolImpl seed = new SeedProtocolImpl();
    private final File res = new File("res");

    @Rule
    public final WithFileManager dir = new WithFileManager(res);

    @Before
    public void setUpStreams() throws IOException {
        outContent = new ByteArrayOutputStream();
        inContent = new ByteArrayOutputStream();
        fm = dir.getFileManager();
    }

    @Test
    public void formStat() throws Exception {
        client.sendStatRequest(new DataOutputStream(outContent), 1);
        outIn = new DataInputStream(new ByteArrayInputStream(outContent.toByteArray()));
        seed.formResponse(outIn, new DataOutputStream(inContent), fm);
        outIn = new DataInputStream(new ByteArrayInputStream(inContent.toByteArray()));
        Set<Integer> lst = new HashSet<>(client.readStatResponse(outIn));
        assertEquals(fm.getTorrentFile(1).getParts(), lst);
    }

    @Test
    public void formGet() throws Exception {
        client.sendGetRequest(new DataOutputStream(outContent), 1, 0);
        outIn = new DataInputStream(new ByteArrayInputStream(outContent.toByteArray()));
        seed.formResponse(outIn, new DataOutputStream(inContent), fm);
        outIn = new DataInputStream(new ByteArrayInputStream(inContent.toByteArray()));
        byte [] bufRes = new byte[fm.getTorrentFile(1).partSize(0)];
        byte [] realBuf = new byte[fm.getTorrentFile(1).partSize(0)];
        int resSize = fm.getTorrentFile(1).read(realBuf, 0);
        client.readGetResponse(outIn, bufRes);
        assertEquals(realBuf, bufRes);
    }

}