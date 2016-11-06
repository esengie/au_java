package ru.spbau.mit.TorrentClient.TorrentFile;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import ru.spbau.mit.Protocol.Client.ClientProtocol;
import ru.spbau.mit.Protocol.Client.ClientProtocolImpl;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;

import static org.junit.Assert.*;

public class FileManagerTest {
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
    private final ClientProtocol prot = new ClientProtocolImpl();

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
    public void getFileIds() throws Exception {

    }

    @Test
    public void getTorrentFile() throws Exception {

    }

    @Test
    public void createTorrentFile() throws Exception {

    }

    @Test
    public void saveToDisk() throws Exception {

    }

}