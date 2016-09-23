package ru.spbau.mit.AsdCommand;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import ru.spbau.mit.AsdCommand.Exceptions.AlreadyAnAsdFolderException;

public class InitCommandTest {

    @Rule
    public final TemporaryFolder folder = new TemporaryFolder();

    @Test(expected = AlreadyAnAsdFolderException.class)
    public void run() throws Exception {
        String t = folder.getRoot().getAbsolutePath();
        System.setProperty("user.dir", t);
        AsdCommand init = AsdCommandFactory.createCommand("init");
        init.run(null, null, null);
        init.run(null, null, null);
    }

}