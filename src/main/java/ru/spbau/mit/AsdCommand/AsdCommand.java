package ru.spbau.mit.AsdCommand;

import ru.spbau.mit.AsdCommand.Exceptions.AlreadyAnAsdFolderException;
import ru.spbau.mit.Revisions.RevisionTree.RevisionTree;
import ru.spbau.mit.Staging.Staging;

import java.io.IOException;

abstract public class AsdCommand {
    AsdCommand(){
    }

    public void run(RevisionTree a_tree, Staging a_staging) throws IOException, AlreadyAnAsdFolderException {
        throw new NoSuchMethodError("Not implemented");
    }
}
