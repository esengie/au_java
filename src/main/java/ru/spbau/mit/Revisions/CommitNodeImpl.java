package ru.spbau.mit.Revisions;

public class CommitNodeImpl implements CommitNode {
    private String m_message;
    private int m_revisionNumber;
    private AsdBranch m_branch;

    public CommitNodeImpl(String a_message, int a_revisionNumber, AsdBranch a_branch) {
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
