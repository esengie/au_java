package ru.spbau.mit.Staging;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import ru.spbau.mit.Revisions.CommitNodes.CommitNode;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PersistentStagingTest {
    @Rule
    public final TemporaryFolder folder = new TemporaryFolder();

    private File f;
    private File dir;
    private File f2;

    @Before
    public void before() throws IOException {
        folder.newFolder(F1DIR.split("/")[0]);
        folder.newFolder(F1DIR);
        f = folder.newFile(F1DIR + "/" + F1);
        dir = folder.newFolder(F2DIR);
        f2 = folder.newFile(F2DIR + "/" + F2);
        System.setProperty("user.dir", folder.getRoot().toString());
        staging = new PersistentStaging(folder.getRoot().toPath());
    }

    private final static String F1DIR = "adm/adm";

    private final static String F1 = "long.txt";
    private final static String F2 = "short.txt";
    private final static String F2DIR = F1DIR + "/1";

    private Staging staging;
    private String dataWritten = "Asdsadas";

    private CommitNode mockNode(int i){
        CommitNode commitNode = mock(CommitNode.class);
        when(commitNode.getRevisionNumber()).thenReturn(i);
        return commitNode;
    }

    private void commit(int i) throws IOException {
        CommitNode commitNode = mockNode(i);
        staging.commitToDisk(commitNode);
    }

    @Test
    public void checkout() throws Exception {
        FileUtils.deleteQuietly(f);
        FileUtils.writeStringToFile(f2, dataWritten + dataWritten + dataWritten, Charset.defaultCharset());

        assertFalse(f.exists());
        assertTrue(f2.exists());
        assertNotEquals(dataWritten, FileUtils.readFileToString(f2, Charset.defaultCharset()));

        staging.checkout(mockNode(0));

        assertTrue(f.exists());
        assertTrue(f2.exists());
        assertEquals("", FileUtils.readFileToString(f2, Charset.defaultCharset()));
    }


    @Test
    public void statusResetRemove() throws Exception {
        staging.remove(f.toPath());
        FileUtils.writeStringToFile(f2, dataWritten + dataWritten + dataWritten, Charset.defaultCharset());
        RepoStatus st = staging.status();
        assertEquals(new ArrayList<>(), st.added());
        assertEquals(new ArrayList<>(), st.modifiedAdded());
        assertEquals(Arrays.asList(F2DIR + "/" + F2), st.modifiedUnAdded());
        assertEquals(Arrays.asList(F1DIR + "/" + F1), st.removed());
        assertEquals(new ArrayList<>(), st.untracked());

        staging.reset(f.toPath());
        st = staging.status();
        assertEquals(new ArrayList<>(), st.removed());
    }

}