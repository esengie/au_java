package ru.spbau.mit.App;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import static org.junit.Assert.*;

public class AsdTest {

    @Rule
    public final TemporaryFolder folder = new TemporaryFolder();

    @Before
    public void before(){
        System.setProperty("user.dir", folder.getRoot().getAbsolutePath());
    }

    @Test
    public void main() throws Exception {
        Asd.main();
    }

}