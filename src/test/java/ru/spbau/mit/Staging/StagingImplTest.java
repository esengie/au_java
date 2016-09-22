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
        stagingLocation = folder.getRoot() + "/" + SaveDirLocation.getFolderName() + "/staging/";
        commitLocation = folder.getRoot() + "/" + SaveDirLocation.getFolderName() + "/0/";
        commitNode = mock(CommitNodeImpl.class);
        when(commitNode.getRevisionNumber()).thenReturn(0);
        staging = new StagingImpl(folder.getRoot().toPath());
    }

    private final static String s1 = "adm";
    private final static String DIR = "adm/adm";

    private final static String F1 = "long.txt";
    private final static String F2 = "short.txt";
    private final static String F2DIR = DIR + "/1";

    private Staging staging;
    private CommitNode commitNode;
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

        staging.commitToDisk(commitNode);

        File file = new File(commitLocation + DIR + "/" + F1);
        assertTrue(file.exists());
        file = new File(commitLocation + F2DIR + "/" + F2);
        assertTrue(file.exists());
        assertEquals(FileUtils.readFileToString(file), dataWritten);

    }

    @Test
    public void checkout() throws Exception {
        commitToDisk();

        FileUtils.deleteQuietly(new File(folder.getRoot() + "/" + DIR + "/" + F1));
        FileUtils.writeStringToFile(new File(folder.getRoot() + "/"  + F2DIR + "/" + F2),
                dataWritten + dataWritten + dataWritten);

        File file = new File(folder.getRoot() + "/"  + DIR + "/" + F1);
        assertFalse(file.exists());
        file = new File(folder.getRoot() + "/"  + F2DIR + "/" + F2);
        assertTrue(file.exists());
        assertNotEquals(FileUtils.readFileToString(file), dataWritten);

        staging.checkout(commitNode);

        file = new File(folder.getRoot() + "/"  + DIR + "/" + F1);
        assertTrue(file.exists());
        file = new File(folder.getRoot() + "/"  + F2DIR + "/" + F2);
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