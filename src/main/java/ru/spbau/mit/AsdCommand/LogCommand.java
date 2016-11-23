package ru.spbau.mit.AsdCommand;

import com.beust.jcommander.Parameters;
import ru.spbau.mit.Revisions.CommitNodes.CommitNode;
import ru.spbau.mit.Revisions.RevisionTree.RevisionTree;
import ru.spbau.mit.Staging.Staging;

import java.io.PrintStream;
import java.util.List;

@Parameters(commandDescription = "Show commit log")
public class LogCommand implements AsdCommand {

    protected LogCommand() {
    }

    @Override
    public void run(RevisionTree tree, Staging staging, PrintStream writer) {
        List<CommitNode> log = tree.getLogPath();

        for (CommitNode c : log) {
            writer.println(String.format("Revision: %d, Branch: %s\n" +
                            "    message: %s",
                    c.getRevisionNumber(), c.getBranch().getName(), c.getMessage()));
        }
    }
}
