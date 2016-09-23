package ru.spbau.mit.Revisions.RevisionTree;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Plain and simple, nothing fancy, code from StackOverflow
 */
public interface RevisionTreeSerializer {
    void serialize(RevisionTree a_tree, OutputStream a_out) throws IOException;
    RevisionTree deserialize(InputStream a_in) throws IOException;
}
