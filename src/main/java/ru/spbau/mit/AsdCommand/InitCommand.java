package ru.spbau.mit.AsdCommand;

import com.beust.jcommander.Parameters;
import ru.spbau.mit.AsdCommand.Exceptions.AlreadyAnAsdFolderException;
import ru.spbau.mit.Revisions.RevisionTree.RevisionTree;
import ru.spbau.mit.Revisions.RevisionTree.RevisionTreeImpl;
import ru.spbau.mit.Staging.Staging;
import ru.spbau.mit.Staging.StagingImpl;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Paths;

import static ru.spbau.mit.Paths.AsdFolderOperations.isAnAsdFolder;

@Parameters(commandDescription = "Initialise the repo (if uninitialised)")
public class InitCommand extends AsdCommand {

    protected InitCommand() {
        super();
    }

    @Override
    public void run(RevisionTree a_tree, Staging a_staging, PrintStream a_writer) throws IOException, AlreadyAnAsdFolderException {
        if (isAnAsdFolder()) {
            throw new AlreadyAnAsdFolderException();
        }
        a_staging = new StagingImpl(Paths.get("").toFile().getAbsoluteFile().toPath());
        a_tree = new RevisionTreeImpl();
    }
}