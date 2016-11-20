package ru.spbau.mit.Server;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import ru.spbau.mit.Client.Client;
import ru.spbau.mit.Client.ClientImpl;
import ru.spbau.mit.Protocol.RemoteFile;
import ru.spbau.mit.Protocol.SimFTPProtocol;
import ru.spbau.mit.Protocol.SimFTPProtocolImpl;
import ru.spbau.mit.Protocol.SimFTPProtocolImplTest;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ServerImplTest {
    @Rule
    public final TemporaryFolder folder = new TemporaryFolder();

    private final  String s1 = "adm";
    private final  String DIR = "adm/adm";

    private final String F1 = "long.txt";
    private final String F2 = "short.txt";
    private final String F2DIR = DIR + "/1";

    private File f;
    private File f2;
    private File dir;


    private ByteArrayOutputStream outContent;
    private DataInputStream inContent;
    private Socket sock;
    private final SimFTPProtocol prot = new SimFTPProtocolImpl();
    private final short portNumber = 1234;

    @Before
    public void setUpStreams() throws IOException {
        outContent = new ByteArrayOutputStream();
        folder.newFolder(DIR.split("/")[0]);
        folder.newFolder(DIR);
        f = folder.newFile(DIR + "/" + F1);
        dir = folder.newFolder(F2DIR);
        f2 = folder.newFile(F2DIR + "/" + F2);
        FileUtils.writeByteArrayToFile(f2, F2DIR.getBytes());
        System.setProperty("user.dir", folder.getRoot().getAbsolutePath());
    }


    @Test
    public void Test() throws Exception {
        Server server = new ServerImpl();
        server.start(portNumber);

        Thread.sleep(200);
        Client client = new ClientImpl();
        client.connect("localhost", portNumber);

        List<RemoteFile> lst = client.executeList(dir.getAbsolutePath());

        assertEquals(f2.getAbsolutePath(), lst.get(0).path);

        client.executeGet(lst.get(0).path, outContent);

        assertEquals(F2DIR, outContent.toString());
    }

}