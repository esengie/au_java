package ru.spbau.mit.App;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import static org.junit.Assert.assertEquals;

import java.io.*;

public class AsdTest {

    @Rule
    public final TemporaryFolder folder = new TemporaryFolder();

    @Before
    public void before() throws IOException {
        FileUtils.copyDirectory(new File("res"), folder.getRoot());
        System.setProperty("user.dir", folder.getRoot().getAbsolutePath());
    }

    @Test
    public void intergrationTest() throws IOException {
        String inp1 = "init\n" +
                "branch left\n" +
                "checkout left\n" +
                "add README.md\n" +
                "add src\n" +
                "commit -m \"asdf\"\n" +
                "checkout master\n" +
                "merge left\n" +
                "rm README.md\n" +
                "status\n" +
                "commit -m \"Lol\"\n" +
                "status\n" +
                "log";
        String output1 = "Newly Added:\n" +
                "Modified Added:\n" +
                "Modified UnAdded:\n" +
                "Untracked:\n" +
                "Removed:\n" +
                "  README.md\n" +
                "Newly Added:\n" +
                "Modified Added:\n" +
                "Modified UnAdded:\n" +
                "Untracked:\n" +
                "Removed:\n" +
                "Revision: 3, Branch: master\n" +
                "    message: Lol\n" +
                "Revision: 2, Branch: master\n" +
                "    message: merged: left into: master\n" +
                "Revision: 0, Branch: master\n" +
                "    message: Init Commit contains everything before init\n";

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayInputStream in = new ByteArrayInputStream(inp1.getBytes());
        System.setIn(in);
        System.setOut(new PrintStream(out));
        AsdVersionControlSystem.main();

        assertEquals(output1, out.toString());

        String inp2 = "commit -m \"asdsadassdf\"\n" +
                "log\n" +
                "add README.md\n" +
                "status";

        String output2 = "Revision: 4, Branch: master\n" +
                "    message: asdsadassdf\n" +
                "Revision: 3, Branch: master\n" +
                "    message: Lol\n" +
                "Revision: 2, Branch: master\n" +
                "    message: merged: left into: master\n" +
                "Revision: 0, Branch: master\n" +
                "    message: Init Commit contains everything before init\n" +
                "Newly Added:\n" +
                "  README.md\n" +
                "Modified Added:\n" +
                "Modified UnAdded:\n" +
                "  gradlew.bat\n" +
                "Untracked:\n" +
                "  asd\n" +
                "Removed:\n";

        out = new ByteArrayOutputStream();
        in = new ByteArrayInputStream(inp2.getBytes());
        System.setIn(in);
        System.setOut(new PrintStream(out));

        folder.newFile("asd");
        FileUtils.writeByteArrayToFile(new File("README.md").getAbsoluteFile(), "asd".getBytes());
        FileUtils.writeByteArrayToFile(new File("gradlew.bat").getAbsoluteFile(), "asd".getBytes());

        AsdVersionControlSystem.main();
        assertEquals(output2, out.toString());
    }
}