package ru.spbau.mit.Revisions.Branches;

import ru.spbau.mit.Revisions.Branches.AsdBranch;

public class AsdBranchImpl implements AsdBranch {
    String m_name;

    public AsdBranchImpl(String a_name) {
        m_name = a_name;
    }

    @Override
    public String getName() {
        return m_name;
    }
}
