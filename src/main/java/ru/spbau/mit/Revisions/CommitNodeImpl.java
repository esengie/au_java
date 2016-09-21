package ru.spbau.mit.Revisions;

import java.util.*;

public class CommitNodeImpl implements CommitNode {
    private String m_author;
    private String m_hashCode;
    private AsdBranch m_branch;

    public CommitNodeImpl(String a_author, String a_hashCode, AsdBranch a_branch) {
        m_author = a_author;
        m_hashCode = a_hashCode;
        m_branch = a_branch;
    }

    @Override
    public String getAuthor() {
        return m_author;
    }

    @Override
    public String getHashCode() {
        return m_hashCode;
    }

    @Override
    public AsdBranch getBranch() {
        return m_branch;
    }
}
