package ru.spbau.mit.Staging;

import ru.spbau.mit.Revisions.CommitNode;

import java.io.IOException;
import java.nio.file.Path;

public interface Staging {
    void add(Path a_file) throws IOException;
    void commitToDisk(CommitNode a_node) throws IOException;
    void emptyStagingArea() throws IOException;
}
