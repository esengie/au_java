package ru.spbau.mit.AsdCommand;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import ru.spbau.mit.Revisions.RevisionTree.RevisionTree;
import ru.spbau.mit.Staging.Staging;

@Parameters(commandDescription = "Add files to the index")
public class AddCommand extends AsdCommand {
    @Parameter(description = "File patterns to add to the index")
    private List<String> patterns;

    protected AddCommand() {
        super();
    }

    @Override
    public void run(RevisionTree a_tree, Staging a_staging, PrintStream a_writer) throws IOException {
        for (String s : new HashSet<>(patterns)) {
            a_staging.add(Paths.get(s));
        }
    }
}
