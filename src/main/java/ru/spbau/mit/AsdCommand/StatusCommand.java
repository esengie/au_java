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
    public void run(RevisionTree a_tree, Staging a_staging, PrintStream a_writer) throws IOException {
        RepoStatus stats = a_staging.status();

        a_writer.println("Added:");
        printList(stats.added(), a_writer);

        a_writer.println("Modified:");
        printList(stats.modified(), a_writer);

        a_writer.println("Untracked:");
        printList(stats.untracked(), a_writer);

        a_writer.println("Removed:");
        printList(stats.removed(), a_writer);
    }

    private void printList(List<String> list, PrintStream writer){
        for (String m : list){
            writer.println("  " + m);
        }
    }
}
