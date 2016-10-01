package ru.spbau.mit.App;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.organicdesign.fp.collections.PersistentTreeMap;
import org.organicdesign.fp.collections.PersistentTreeSet;
import ru.spbau.mit.Revisions.RevisionTree.RevisionTree;

import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TreeMap;

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
//        System.setIn(in);
        byte[] s = FileUtils.readFileToByteArray(new File("geom.pdf").getAbsoluteFile());

        PersistentTreeMap<String, byte[]> tst = PersistentTreeMap.empty();
        tst = tst.assoc("one", s);

        TreeMap<String, byte[]> tst2 = new TreeMap<>();
        tst2.put("one", s);

        List<PersistentTreeMap<String, byte[]>> states = new ArrayList<>();

        states.add(tst);
        tst = tst.assoc("two", "asdasdsa".getBytes());
        tst2.put("two", "asdasdsa".getBytes());

        states.add(tst);

        byte [] s2 = Arrays.copyOf(s, s.length);
        tst = tst.assoc("three", s2);
        tst2.put("three", s2);

        states.add(tst);

        OutputStream a_out = new FileOutputStream(new File("aaaaa").getAbsoluteFile());
        InputStream a_in = new FileInputStream(new File("aaaaa").getAbsoluteFile());

        ObjectOutputStream out = new ObjectOutputStream(a_out);
        out.writeObject(states);
        out.close();


        ObjectInputStream ino = new ObjectInputStream(a_in);
        List<PersistentTreeMap<String, byte[]>> retVal = null;
        retVal = (ArrayList<PersistentTreeMap<String, byte[]>>) ino.readObject();

//        assertEquals(retVal, states);

        FileUtils.writeByteArrayToFile(new File("geom2.pdf").getAbsoluteFile(), retVal.get(0).get("one"));
//        AsdVersionControlSystem.main();
//        FileUtils.deleteDirectory(asd.folder);
    }

}