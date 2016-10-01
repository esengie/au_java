package ru.spbau.mit.AsdCommand;

import ru.spbau.mit.Revisions.RevisionTree.RevisionTree;
import ru.spbau.mit.Staging.Staging;

import java.io.IOException;
import java.io.PrintStream;


/**
 * The interface of all the Commands.
 * <p>
 * Commands get parsed by JCommander for more coupling and less stability
 */
public interface AsdCommand {
    /**
     * Generic run interface
     *
     * @param tree    A Revision tree
     * @param staging The Staging and disk class
     * @param writer  OutPutStream
     * @throws IOException may throw depending on staging class implementation
     */
    void run(RevisionTree tree, Staging staging, PrintStream writer) throws IOException;
}
