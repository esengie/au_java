package ru.spbau.mit.Revisions.CommitNodes;


import ru.spbau.mit.Revisions.RevisionTree.RevisionTree;

public class CommitNodeFactory {
    private CommitNodeFactory() {

    }

    public static CommitNode createNode(RevisionTree a_tree, String a_message) {
        CommitNode retVal = new CommitNodeImpl(a_tree.getCurrentBranch(),
                a_tree.getRevisionNumber(),
                a_message);
        return retVal;
    }
}

