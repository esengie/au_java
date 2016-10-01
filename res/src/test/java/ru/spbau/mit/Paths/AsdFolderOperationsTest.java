package ru.spbau.mit.Paths;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.*;

public class AsdFolderOperationsTest {
    @Rule
    public final TemporaryFolder folder = new TemporaryFolder();

    private final static String s1 = "adm";
    private final static String DIR = "adm/adm";

    private final static String F1 = "long.txt";
    private final static String F2 = "short.txt";
    private final static String F2DIR = DIR + "/1";

    @Before
    public void before() throws IOException {
        folder.newFolder(DIR.split("/")[0]);
        folder.newFolder(DIR);
        File f = folder.newFile(DIR + "/" + F1);
        File dir = folder.newFolder(F2DIR);
        File f2 = folder.newFile(F2DIR + "/" + F2);
    }

    @Ignore
    @Test
    public void getSerializedTreePath() throws Exception {

    }

    @Test
    public void isAnAsdFolder() throws Exception {
        assertFalse(AsdFolderOperations.isAnAsdFolder());

        System.setProperty("user.dir", folder.getRoot().getAbsolutePath());
        assertFalse(AsdFolderOperations.isAnAsdFolder());

        folder.newFolder(SaveDirLocation.getFolderName());

        assertTrue(AsdFolderOperations.isAnAsdFolder());

        System.setProperty("user.dir", folder.getRoot().getAbsolutePath() + "/" + F2DIR);

        assertTrue(AsdFolderOperations.isAnAsdFolder());
    }

    @Test
    public void getRoot() throws Exception {
        assertNull(AsdFolderOperations.getRoot());

        System.setProperty("user.dir", folder.getRoot().getAbsolutePath());
        assertNull(AsdFolderOperations.getRoot());

        folder.newFolder(SaveDirLocation.getFolderName());

        assertEquals(AsdFolderOperations.getRoot().toString(),
                folder.getRoot().getAbsolutePath());

        System.setProperty("user.dir", folder.getRoot().getAbsolutePath() + "/" + F2DIR);

        assertEquals(AsdFolderOperations.getRoot().toString(),
                folder.getRoot().getAbsolutePath());

    }

}