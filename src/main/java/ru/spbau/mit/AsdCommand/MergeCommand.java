package ru.spbau.mit.AsdCommand;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import ru.spbau.mit.App.Exceptions.TooManyArgumentsException;
import ru.spbau.mit.AsdCommand.Exceptions.TooFewArgumentsException;
import ru.spbau.mit.Revisions.Branches.AsdBranch;
import ru.spbau.mit.Revisions.Branches.AsdBranchFactory;
import ru.spbau.mit.Revisions.CommitNodes.CommitNode;
import ru.spbau.mit.Revisions.CommitNodes.CommitNodeFactory;
import ru.spbau.mit.Revisions.RevisionTree.RevisionTree;
import ru.spbau.mit.Staging.Staging;

import java.io.IOException;
import java.io.PrintStream;
import java.util.List;

@Parameters(commandDescription = "Merge a branch to this one")
public class MergeCommand implements AsdCommand {
    @Parameter(description = "The branch name")
    private List<String> branchName;

    protected MergeCommand() {
    }

    @Override
    public void run(RevisionTree tree, Staging staging, PrintStream writer) throws IOException {
        if (branchName == null)
            throw new TooFewArgumentsException("merge needs a branch name to merge");
        if (branchName.size() > 1)
            throw new TooManyArgumentsException("merge needs one argument");

        CommitNode into = tree.getHeadOfBranch(tree.getCurrentBranch());
        AsdBranch fromBranch = AsdBranchFactory.createBranch(branchName.get(0));
        CommitNode from = tree.getHeadOfBranch(fromBranch);

        CommitNode result = CommitNodeFactory.createNode(tree,
                "merged: " + from.getBranch().getName() +
                        " into: " + into.getBranch().getName());

        if (tree.isEarlierThanCurrent(fromBranch)) {
            writer.println("Already up-to date");
            return;
        }

        staging.merge(from, into, result);
        tree.merge(fromBranch, result);
    }
}
