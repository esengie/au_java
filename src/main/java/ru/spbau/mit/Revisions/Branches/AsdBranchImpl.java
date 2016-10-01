package ru.spbau.mit.Revisions.Branches;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import ru.spbau.mit.Revisions.Branches.AsdBranch;
import ru.spbau.mit.Revisions.RevisionTree.RevisionTreeImpl;

public class AsdBranchImpl implements AsdBranch {
    private final String m_name;

    public AsdBranchImpl(String a_name) {
        m_name = a_name;
    }

    @Override
    public String getName() {
        return m_name;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 31).
                append(getName()).
                toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof AsdBranch))
            return false;
        if (obj == this)
            return true;

        AsdBranch rhs = (AsdBranch) obj;
        return new EqualsBuilder().
                append(getName(), rhs.getName()).
                isEquals();
    }
}
