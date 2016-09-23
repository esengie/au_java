package ru.spbau.mit.Revisions.CommitNodes;


import ru.spbau.mit.Revisions.Exceptions.ThePreviousCommitNodeWasnotPassedToTheTreeError;
import ru.spbau.mit.Revisions.RevisionTree.RevisionTree;

import java.util.HashMap;
import java.util.Map;

public class CommitNodeFactory {
    private CommitNodeFactory() {

    }
    private static Map<RevisionTree, Integer> m_map = new HashMap<>();

    public static CommitNode createNode(RevisionTree a_tree, String a_message) {
        int revisionNumber = a_tree.getRevisionNumber();
        if (m_map.getOrDefault(a_tree, -1) >= revisionNumber)
            throw new ThePreviousCommitNodeWasnotPassedToTheTreeError();
        CommitNode retVal = new CommitNodeImpl(a_tree.getCurrentBranch(),
                revisionNumber,
                a_message);

        m_map.put(a_tree, revisionNumber);
        return retVal;
    }
}

