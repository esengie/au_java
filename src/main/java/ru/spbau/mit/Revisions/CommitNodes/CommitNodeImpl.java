package ru.spbau.mit.Revisions.CommitNodes;

import ru.spbau.mit.Revisions.Branches.AsdBranch;

public class CommitNodeImpl implements CommitNode {
    private String m_message;
    private int m_revisionNumber;
    private AsdBranch m_branch;

    CommitNodeImpl(AsdBranch a_branch, int a_revisionNumber, String a_message) {
        m_message = a_message;
        m_revisionNumber = a_revisionNumber;
        m_branch = a_branch;
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
}
