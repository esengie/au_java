package ru.spbau.mit.App;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

import static org.junit.Assert.*;

public class AsdTest {

//    @Rule
//    public final TemporaryFolder folder = new TemporaryFolder();

    @Before
    public void before() throws IOException {
        File folder = new File("/tmp/junit123");
        folder.mkdir();
        FileUtils.copyDirectory(new File("res"), folder); //.getRoot().getAbsoluteFile()
        System.setProperty("user.dir", folder.getAbsolutePath());
    }

//    @Test
    public static void main(String... argv) throws Exception {
        AsdTest asd = new AsdTest();
        asd.before();
        Asd.main();
    }

}