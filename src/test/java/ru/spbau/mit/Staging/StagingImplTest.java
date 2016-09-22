package ru.spbau.mit.Staging;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import ru.spbau.mit.App.SaveDirLocation;
import ru.spbau.mit.Revisions.CommitNodes.CommitNode;
import ru.spbau.mit.Revisions.CommitNodes.CommitNodeImpl;

import java.io.File;

import static org.junit.Assert.*;

import static org.mockito.Mockito.*;

public class StagingImplTest {
    @Rule
    public final TemporaryFolder folder = new TemporaryFolder();

    @Before
    public void before(){
        System.setProperty("user.dir", folder.getRoot().getAbsolutePath());
        stagingLocation = folder.getRoot() + "/" + SaveDirLocation.getFolderName() + "/staging/";
        commitLocation = folder.getRoot() + "/" + SaveDirLocation.getFolderName() + "/0/";
    }

    private final static String s1 = "adm";
    private final static String DIR = "adm/adm";

    private final static String F1 = "long.txt";
    private final static String F2 = "short.txt";
    private final static String F2DIR = DIR + "/1";

    private Staging staging;
    private String dataWritten;
    private String stagingLocation;
    private String commitLocation;

    @Test
    public void add() throws Exception {
        folder.newFolder(DIR.split("/")[0]);
        folder.newFolder(DIR);
        File f = folder.newFile(DIR + "/" + F1);
        File dir = folder.newFolder(F2DIR);
        File f2 = folder.newFile(F2DIR + "/" + F2);

        staging = new StagingImpl();

        staging.add(f.toPath());
        staging.add(dir.toPath());

        dataWritten = "lalalalalalal";
        FileUtils.writeStringToFile(f2, dataWritten);
        staging.add(f2.toPath());

        File file = new File(stagingLocation + DIR + "/" + F1);
        assertTrue(file.exists());
        file = new File(stagingLocation + F2DIR + "/" + F2);
        assertTrue(file.exists());

        assertEquals(FileUtils.readFileToString(file), dataWritten);
    }

    @Test
    public void commitToDisk() throws Exception {
        add();
        CommitNode c = mock(CommitNodeImpl.class);
        when(c.getRevisionNumber()).thenReturn(0);
        staging.commitToDisk(c);

        File file = new File(commitLocation + DIR + "/" + F1);
        assertTrue(file.exists());
        file = new File(commitLocation + F2DIR + "/" + F2);
        assertTrue(file.exists());
        assertEquals(FileUtils.readFileToString(file), dataWritten);

    }

    @Test
    public void emptyStagingArea() throws Exception {
        add();
        staging.emptyStagingArea();
        assertEquals(new File(stagingLocation).listFiles().length, 0);
    }

}