package ru.spbau.mit.AsdCommand;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import ru.spbau.mit.Revisions.RevisionTree.RevisionTree;
import ru.spbau.mit.Staging.Staging;

import java.io.IOException;
import java.util.List;

@Parameters(commandDescription = "Merge a branch to this one")
public class MergeCommand extends AsdCommand {
    @Parameter(description = "The branch name")
    private List<String> branchName;

    protected MergeCommand(){super();}

    @Override
    public void run(RevisionTree a_tree, Staging a_staging) throws IOException {
//        super.run();
    }
}
