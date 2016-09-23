package ru.spbau.mit.AsdCommand;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import ru.spbau.mit.App.Exceptions.TooManyArgumentsException;
import ru.spbau.mit.Revisions.Branches.AsdBranch;
import ru.spbau.mit.Revisions.Branches.AsdBranchFactory;
import ru.spbau.mit.Revisions.RevisionTree.RevisionTree;
import ru.spbau.mit.Staging.Staging;

import java.io.IOException;
import java.io.PrintStream;
import java.util.List;

@Parameters(commandDescription = "Branch managements")
public class BranchCommand extends AsdCommand {

    @Parameter(description = "The branch name")
    private List<String> branchName;

    @Parameter(names = "-d", description = "delete branch")
    private boolean deleteMode = false;

    protected BranchCommand(){super();}

    @Override
    public void run(RevisionTree a_tree, Staging a_staging, PrintStream a_writer) throws IOException {
        if (branchName.size() > 1)
            throw new TooManyArgumentsException("branch command needs only one branch");
        if (branchName.size() == 1){
            a_tree.branchCreate(AsdBranchFactory.createBranch(branchName.get(0)));
            return;
        }

        for (AsdBranch b : a_tree.getBranches()){
            a_writer.println(b.getName());
        }
    }
}
