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
     * @param a_tree    A Revision tree
     * @param a_staging The Staging and disk class
     * @param a_writer  OutPutStream
     * @throws IOException IOexceptions may happen
     */
    void run(RevisionTree a_tree, Staging a_staging, PrintStream a_writer) throws IOException;
}
