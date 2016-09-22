package ru.spbau.mit.AsdCommand;

import com.beust.jcommander.Parameters;
import ru.spbau.mit.AsdCommand.Exceptions.AlreadyAnAsdFolderException;
import ru.spbau.mit.Revisions.RevisionTree.RevisionTree;
import ru.spbau.mit.Revisions.RevisionTree.RevisionTreeImpl;
import ru.spbau.mit.App.SaveDirLocation;
import ru.spbau.mit.Staging.Staging;
import ru.spbau.mit.Staging.StagingImpl;

import java.io.File;
import java.io.IOException;

@Parameters(commandDescription = "Initialise the repo (if uninitialised)")
public class InitCommand extends AsdCommand {

    protected InitCommand() {
        super();
    }

    @Override
    public void run() throws IOException, AlreadyAnAsdFolderException {
        File f = new File(SaveDirLocation.getFolderName());
        if (f.getAbsoluteFile().exists()) {
            throw new AlreadyAnAsdFolderException();
        }
        Staging staging = new StagingImpl();
        RevisionTree revTree = new RevisionTreeImpl();
    }
}