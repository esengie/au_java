package ru.spbau.mit.AsdCommand;

import ru.spbau.mit.Revisions.RevisionTree.RevisionTree;
import ru.spbau.mit.Staging.Staging;

import java.io.IOException;
import java.io.PrintStream;


/**
 * The interface of all the Commands.
 *
 * Commands get parsed by JCommander for more coupling and less stability
 */
public interface AsdCommand {
    void run(RevisionTree a_tree, Staging a_staging, PrintStream a_writer) throws IOException;
}
