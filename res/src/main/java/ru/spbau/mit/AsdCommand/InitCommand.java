package ru.spbau.mit.AsdCommand;

import com.beust.jcommander.Parameters;
import org.apache.commons.lang.NotImplementedException;
import ru.spbau.mit.Revisions.RevisionTree.RevisionTree;
import ru.spbau.mit.Staging.Staging;

import java.io.IOException;
import java.io.PrintStream;

@Parameters(commandDescription = "Initialise the repo (if uninitialised)")
public class InitCommand implements AsdCommand {

    protected InitCommand() {
    }

    @Override
    public void run(RevisionTree a_tree, Staging a_staging, PrintStream a_writer) throws IOException {
        // Cause java copies references
        throw new NotImplementedException("Shouldn't be here at all");
    }
}