package ru.spbau.mit.AsdCommand;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import ru.spbau.mit.Revisions.RevisionTree.RevisionTree;
import ru.spbau.mit.Staging.Staging;

import java.io.IOException;
import java.io.PrintStream;
import java.util.List;

@Parameters(commandDescription = "Record changes to the repository")
public class CommitCommand extends AsdCommand {
    protected CommitCommand(){super();}

    @Parameter(names = "-m", description = "Commit message", required = true)
    private String message = "";

    @Override
    public void run(RevisionTree a_tree, Staging a_staging, PrintStream a_writer) throws IOException {
        System.out.println(message);
    }
}
