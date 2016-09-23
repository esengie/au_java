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

    File folder;
    @Before
    public void before() throws IOException {
        folder = new File("/tmp/junit123");
        folder.mkdir();
        FileUtils.copyDirectory(new File("res"), folder); //.getRoot().getAbsoluteFile()
        System.setProperty("user.dir", folder.getAbsolutePath());
    }

//    @Test
    public static void main(String... argv) throws Exception {
        AsdTest asd = new AsdTest();
        asd.before();

        String inp = "init\n" +
                "branch left\n" +
                "checkout left\n" +
                "add README.md\n" +
                "add src\n" +
                "commit -m \"asdf\"\n" +
                "checkout master\n" +
                "merge left\n" +
                "log";

        ByteArrayInputStream in = new ByteArrayInputStream(inp.getBytes());
        System.setIn(in);

        Asd.main();
//        FileUtils.deleteDirectory(asd.folder);
    }

}