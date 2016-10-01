package ru.spbau.mit.AsdCommand;

import com.beust.jcommander.Parameters;
import ru.spbau.mit.Revisions.RevisionTree.RevisionTree;
import ru.spbau.mit.Staging.Staging;

import java.io.IOException;
import java.io.PrintStream;

@Parameters(commandDescription = "Clean staging area")
public class CleanCommand implements AsdCommand {

    protected CleanCommand() {
    }


    /**
     * Checkout the latest commit in this branch
     *
     * @param a_tree    A Revision tree
     * @param a_staging The Staging and disk class
     * @param a_writer  OutPutStream
     * @throws IOException may throw depending on staging class implementation
     */
    @Override
    public void run(RevisionTree a_tree, Staging a_staging, PrintStream a_writer) throws IOException {
        a_staging.checkout(a_tree.getHeadOfBranch(a_tree.getCurrentBranch()));
    }
}
