package ru.spbau.mit.AsdCommand;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import ru.spbau.mit.App.Exceptions.TooManyArgumentsException;
import ru.spbau.mit.Revisions.Branches.AsdBranch;
import ru.spbau.mit.Revisions.Branches.AsdBranchFactory;
import ru.spbau.mit.Revisions.Exceptions.BranchAlreadyExistsException;
import ru.spbau.mit.Revisions.RevisionTree.RevisionTree;
import ru.spbau.mit.Staging.Staging;

import java.io.IOException;
import java.io.PrintStream;
import java.util.List;

@Parameters(commandDescription = "Branch managements")
public class BranchCommand implements AsdCommand {

    @Parameter(description = "The branch name")
    private List<String> branchName;

    protected BranchCommand() {
    }

    @Override
    public void run(RevisionTree tree, Staging staging, PrintStream writer) throws TooManyArgumentsException, BranchAlreadyExistsException {
        if (branchName == null) {
            for (AsdBranch b : tree.getBranches()) {
                if (b.equals(tree.getCurrentBranch()))
                    writer.println("*" + b.getName());
                else
                    writer.println(" " + b.getName());
            }
            return;
        }

        if (branchName.size() > 1)
            throw new TooManyArgumentsException("branch command needs only one branch");

        tree.branchCreate(AsdBranchFactory.createBranch(branchName.get(0)));
    }
}
