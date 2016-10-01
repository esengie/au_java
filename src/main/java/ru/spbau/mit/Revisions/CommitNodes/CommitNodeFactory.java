package ru.spbau.mit.Revisions.CommitNodes;


import ru.spbau.mit.Revisions.Exceptions.PreviousCommitNodeWasntPassedToTreeRuntimeException;
import ru.spbau.mit.Revisions.RevisionTree.RevisionTree;

import java.util.HashMap;
import java.util.Map;

public class CommitNodeFactory {
    private CommitNodeFactory() {

    }

    private static Map<RevisionTree, Integer> m_map = new HashMap<>();

    public static CommitNode createNode(RevisionTree tree, String message) {
        int revisionNumber = tree.getRevisionNumber();
        if (m_map.getOrDefault(tree, -1) >= revisionNumber)
            throw new PreviousCommitNodeWasntPassedToTreeRuntimeException();
        CommitNode retVal = new CommitNodeImpl(tree.getCurrentBranch(),
                revisionNumber,
                message);

        m_map.put(tree, revisionNumber);
        return retVal;
    }
}

