package ru.spbau.mit.Staging;

import ru.spbau.mit.Revisions.CommitNodes.CommitNode;

import java.io.IOException;
import java.nio.file.Path;

/**
 * The interface that does the actual work:
 * commiting to disk, merging, adding to the staging area
 */
public interface Staging {
    void add(Path file) throws IOException;

    void commitToDisk(CommitNode node) throws IOException;

    void emptyStagingArea() throws IOException;

    void checkout(CommitNode node) throws IOException;

    void merge(CommitNode from, CommitNode to, CommitNode result) throws IOException;

    void reset(Path file) throws IOException;

    void remove(Path file) throws IOException;

    RepoStatus status() throws IOException;
}
