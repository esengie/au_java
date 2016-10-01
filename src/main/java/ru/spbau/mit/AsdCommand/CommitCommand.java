package ru.spbau.mit.AsdCommand;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import ru.spbau.mit.Revisions.CommitNodes.CommitNode;
import ru.spbau.mit.Revisions.CommitNodes.CommitNodeFactory;
import ru.spbau.mit.Revisions.RevisionTree.RevisionTree;
import ru.spbau.mit.Staging.Staging;

import java.io.IOException;
import java.io.PrintStream;

@Parameters(commandDescription = "Record changes to the repository")
public class CommitCommand implements AsdCommand {
    protected CommitCommand() {
    }

    @Parameter(names = "-m", description = "Commit message", required = true)
    private String message = "";

    @Override
    public void run(RevisionTree a_tree, Staging a_staging, PrintStream a_writer) throws IOException {
        CommitNode c = CommitNodeFactory.createNode(a_tree, message);
        a_tree.commit(c);
        a_staging.commitToDisk(c);
    }
}
