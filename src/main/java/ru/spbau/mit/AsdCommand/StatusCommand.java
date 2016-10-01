package ru.spbau.mit.AsdCommand;

import com.beust.jcommander.Parameters;
import ru.spbau.mit.Revisions.RevisionTree.RevisionTree;
import ru.spbau.mit.Staging.RepoStatus;
import ru.spbau.mit.Staging.Staging;

import java.io.IOException;
import java.io.PrintStream;
import java.util.List;

@Parameters(commandDescription = "Gives the status")
public class StatusCommand implements AsdCommand {
    protected StatusCommand() {
    }

    @Override
    public void run(RevisionTree tree, Staging staging, PrintStream writer) throws IOException {
        RepoStatus stats = staging.status();

        writer.println("Newly Added:");
        printList(stats.added(), writer);

        writer.println("Modified Added:");
        printList(stats.modifiedAdded(), writer);

        writer.println("Modified UnAdded:");
        printList(stats.modifiedUnAdded(), writer);

        writer.println("Untracked:");
        printList(stats.untracked(), writer);

        writer.println("Removed:");
        printList(stats.removed(), writer);
    }

    private void printList(List<String> list, PrintStream writer){
        for (String m : list){
            writer.println("  " + m);
        }
    }
}
