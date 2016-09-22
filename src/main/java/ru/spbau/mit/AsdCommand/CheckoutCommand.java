package ru.spbau.mit.AsdCommand;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import ru.spbau.mit.Revisions.RevisionTree.RevisionTree;
import ru.spbau.mit.Staging.Staging;

import java.io.IOException;
import java.util.List;

@Parameters(commandDescription = "Checkout a branch or a commit")
public class CheckoutCommand extends AsdCommand {

    @Parameter(description = "Branch name or revision")
    private List<String> branch;

    protected CheckoutCommand(){super();}

    @Override
    public void run(RevisionTree a_tree, Staging a_staging) throws IOException {
//        super.run();
    }
}
