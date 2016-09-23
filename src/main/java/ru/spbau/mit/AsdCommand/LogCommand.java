package ru.spbau.mit.AsdCommand;

import com.beust.jcommander.Parameters;
import ru.spbau.mit.Revisions.RevisionTree.RevisionTree;
import ru.spbau.mit.Staging.Staging;

import java.io.IOException;
import java.io.PrintStream;

@Parameters(commandDescription = "Show commit log")
public class LogCommand extends AsdCommand {

    protected LogCommand(){super();}

    @Override
    public void run(RevisionTree a_tree, Staging a_staging, PrintStream a_writer) throws IOException {
//        super.run();
    }
}
