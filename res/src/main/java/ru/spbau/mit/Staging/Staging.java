package ru.spbau.mit.Staging;

import ru.spbau.mit.Revisions.CommitNodes.CommitNode;

import java.io.IOException;
import java.nio.file.Path;

public interface Staging {
    void add(Path a_file) throws IOException;
    void commitToDisk(CommitNode a_node) throws IOException;
    void emptyStagingArea() throws IOException;
    void checkout(CommitNode a_node) throws IOException;
}
