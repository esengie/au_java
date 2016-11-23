package ru.spbau.mit.Revisions.CommitNodes;

import ru.spbau.mit.Revisions.Branches.AsdBranch;

public class CommitNodeImpl implements CommitNode {
    private final String m_message;
    private final int m_revisionNumber;
    private final AsdBranch m_branch;

    CommitNodeImpl(AsdBranch branch, int revisionNumber, String message) {
        m_message = message;
        m_revisionNumber = revisionNumber;
        m_branch = branch;
    }

    @Override
    public String getMessage() {
        return m_message;
    }

    @Override
    public int getRevisionNumber() {
        return m_revisionNumber;
    }

    @Override
    public AsdBranch getBranch() {
        return m_branch;
    }

    @Override
    public int hashCode() {
        return hashCoder();
    }

    @Override
    public boolean equals(Object obj) {
        return equalser(obj);
    }
}
