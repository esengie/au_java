package ru.spbau.mit.AsdCommand;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import ru.spbau.mit.App.Exceptions.TooManyArgumentsException;
import ru.spbau.mit.AsdCommand.Exceptions.TooLittleArgumentsException;
import ru.spbau.mit.Revisions.Branches.AsdBranchFactory;
import ru.spbau.mit.Revisions.CommitNodes.CommitNode;
import ru.spbau.mit.Revisions.RevisionTree.RevisionTree;
import ru.spbau.mit.Staging.Staging;

import java.io.IOException;
import java.io.PrintStream;
import java.util.List;

@Parameters(commandDescription = "Checkout a branch or a commit")
public class CheckoutCommand implements AsdCommand {

    @Parameter(description = "Branch name or revision")
    private List<String> input;

    protected CheckoutCommand() {
    }

    @Override
    public void run(RevisionTree a_tree, Staging a_staging, PrintStream a_writer) throws IOException {
        if (input.size() > 1)
            throw new TooManyArgumentsException("checkout needs only one argument");
        if (input.size() == 0)
            throw new TooLittleArgumentsException("checkout needs one argument");

        int revision;
        String arg = input.get(0);
        try {
            revision = Integer.parseInt(arg);
        } catch (NumberFormatException e) {
            revision = -1;
        }

        CommitNode c;
        if (revision == -1)
            c = a_tree.checkout(AsdBranchFactory.createBranch(arg));
        else
            c = a_tree.checkout(revision);

        a_staging.checkout(c);
    }
}
