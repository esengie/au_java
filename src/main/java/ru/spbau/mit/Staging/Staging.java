package ru.spbau.mit.Staging;

import ru.spbau.mit.Revisions.CommitNodes.CommitNode;

import java.io.IOException;
import java.nio.file.Path;

/**
 * The interface that does the actual work:
 * commiting to disk, merging, adding to the staging area
 *
 */
public interface Staging {
    /**
     * Adds the file to the staging area
     *
     * @param file the path to the file
     * @throws IOException can throw, for instance if the file doesn't exist
     */
    void add(Path file) throws IOException;

    /**
     * Moves staging area to the commit area
     *
     * @param node the node (usually the number is used)
     * @throws IOException some implementations may throw
     */
    void commitToDisk(CommitNode node) throws IOException;

    /**
     * Empties the staging are (the one updated with "add")
     *
     * @throws IOException it may throw
     */
    void emptyStagingArea() throws IOException;

    /**
     * Checks out the node specified into the repo directory
     *
     * @param node the commit
     * @throws IOException may throw
     */
    void checkout(CommitNode node) throws IOException;

    /**
     * Performs the actions required to do a physical merge
     *
     * @param from from the node
     * @param to into the node
     * @param result resulting commit
     * @throws IOException throws if it can't merge, may throw because of disk ops
     */
    void merge(CommitNode from, CommitNode to, CommitNode result) throws IOException;

    /**
     * Resets a file to the last known commit in this context
     *
     * @param file filename of a file to reset
     * @throws IOException may throw
     */
    void reset(Path file) throws IOException;

    /**
     * Removes a file from staging and from the repo
     *
     * @param file filename of a file to remove
     * @throws IOException may throw
     */
    void remove(Path file) throws IOException;

    /**
     * Builds the RepoStatus object and returns it for further processing
     *
     * @return a ton of info
     * @throws IOException may throw
     */
    RepoStatus status() throws IOException;
}
