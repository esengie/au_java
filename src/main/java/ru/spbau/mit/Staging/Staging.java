package ru.spbau.mit.Staging;

import ru.spbau.mit.Revisions.CommitNodes.CommitNode;
import ru.spbau.mit.Staging.Exceptions.CantMergeException;

import java.io.IOException;
import java.nio.file.Path;

/**
 * The interface that does the actual work:
 * commiting to disk, merging, adding to the staging area
 */
public interface Staging {
    void add(Path a_file) throws IOException;
    void commitToDisk(CommitNode a_node) throws IOException;
    void emptyStagingArea() throws IOException;
    void checkout(CommitNode a_node) throws IOException;
    void merge(CommitNode a_from, CommitNode a_to, CommitNode a_result) throws IOException;
}
