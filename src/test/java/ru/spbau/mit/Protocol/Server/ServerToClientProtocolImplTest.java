package ru.spbau.mit.Protocol.Server;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import ru.spbau.mit.Protocol.Client.ClientProtocol;
import ru.spbau.mit.Protocol.Client.ClientProtocolImpl;

import java.io.*;
import java.lang.reflect.Array;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class ServerToClientProtocolImplTest {
    private ByteArrayOutputStream outContent;
    private ByteArrayOutputStream inContent;
    private DataInputStream outIn;

    ClientProtocol client = new ClientProtocolImpl();
    ServerProtocol server;

    @Rule
    public final TemporaryFolder folder = new TemporaryFolder();

    @Before
    public void setUpStreams() throws IOException {
        try {
            server = new ServerProtocolImpl(new File(folder.getRoot().getAbsolutePath()));
        } catch (IOException e) {
            // cant happen
        }

        outContent = new ByteArrayOutputStream();
        inContent = new ByteArrayOutputStream();
        client.sendUpdateRequest(new DataOutputStream(outContent), (short)1233, Arrays.asList(1,2));
        outIn = new DataInputStream(new ByteArrayInputStream(outContent.toByteArray()));
        server.formResponse(outIn, new DataOutputStream(inContent), InetAddress.getByName("127.0.0.1"));
        outContent = new ByteArrayOutputStream();
        inContent = new ByteArrayOutputStream();
    }
    @Test
    public void formResponseSources() throws Exception {
        client.sendSourcesRequest(new DataOutputStream(outContent), 1);
        outIn = new DataInputStream(new ByteArrayInputStream(outContent.toByteArray()));
        InetAddress addr = InetAddress.getByName("127.0.0.1");
        server.formResponse(outIn, new DataOutputStream(inContent), addr);
        outIn = new DataInputStream(new ByteArrayInputStream(inContent.toByteArray()));
        List<InetSocketAddress> lst = client.readSourcesResponse(outIn);
        assertEquals(addr, lst.get(0).getAddress());
    }

}