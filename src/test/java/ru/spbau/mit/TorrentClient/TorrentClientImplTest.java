package ru.spbau.mit.TorrentClient;

import junit.framework.TestCase;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import ru.spbau.mit.Common.WithFileManager;
import ru.spbau.mit.TorrentClient.TorrentFile.FileManager;
import ru.spbau.mit.TorrentServer.TorrentServer;
import ru.spbau.mit.TorrentServer.TorrentServerImpl;

import java.io.File;
import java.io.IOException;

import static junit.framework.Assert.assertEquals;
import static junit.framework.TestCase.assertTrue;

/**
 * Interaction test
 */
public class TorrentClientImplTest {
    private FileManager fm1;
    private FileManager fm2;
    private TorrentClient cl1;
    private TorrentClient cl2;
    private final short port1 = 8001;
    private final short port2 = 8002;

    private final File res1 = new File("res");
    private final File res2 = new File("gradle/wrapper");
    private final String largeBin = "zbt.tar.gz";
    private TorrentServer server = new TorrentServerImpl();

    @Rule
    public final TemporaryFolder sDir = new TemporaryFolder();

    @Rule
    public final WithFileManager cDir1 = new WithFileManager(res1, false);

    @Rule
    public final WithFileManager cDir2 = new WithFileManager(res2, false);

    @Before
    public void start() throws IOException, InterruptedException {
        server.start(sDir.newFolder());
        fm1 = cDir1.getFileManager();
        fm2 = cDir2.getFileManager();
        Thread.sleep(100);
        cl1 = new TorrentClientImpl(fm1, port1);
        cl2 = new TorrentClientImpl(fm2, port2);
        cl1.connect("localhost");
        cl2.connect("localhost");
    }

    @After
    public void stop() throws IOException, InterruptedException {
        cl1.disconnect();
        cl2.disconnect();
        Thread.sleep(200);
        server.stop();
    }


    @Test
    public void executeUpload() throws Exception {
        cl1.executeUpload(new File(cDir1.curDir, largeBin));
        assertEquals(1, cl2.executeList().size());
    }

    @Test
    public void executeGet() throws Exception {
        cl1.executeUpload(new File(cDir1.curDir, largeBin));
        cl2.executeGet(cDir2.curDir, cl2.executeList().get(0));
        Thread.sleep(10000);

        TestCase.assertEquals(fm1.getTorrentFile(0).getParts(), fm2.getTorrentFile(0).getParts());
        assertTrue(FileUtils.contentEquals(new File(cDir1.curDir, largeBin),
                new File(cDir2.curDir, largeBin)));
    }

}