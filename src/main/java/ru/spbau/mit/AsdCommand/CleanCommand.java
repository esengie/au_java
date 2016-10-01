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
     * @param tree    A Revision tree
     * @param staging The Staging and disk class
     * @param writer  OutPutStream
     * @throws IOException may throw depending on staging class implementation
     */
    @Override
    public void run(RevisionTree tree, Staging staging, PrintStream writer) throws IOException {
        staging.checkout(tree.getHeadOfBranch(tree.getCurrentBranch()));
    }
}
