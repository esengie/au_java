package ru.spbau.mit.AsdCommand;

import java.io.IOException;
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
    public void run(RevisionTree a_tree, Staging a_staging) throws IOException {
        for (String s : patterns) {
            System.out.println(s);
        }
    }
}
