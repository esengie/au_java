package ru.spbau.mit.Revisions.Branches;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

class AsdBranchImpl implements AsdBranch {
    private final String m_name;

    AsdBranchImpl(String name) {
        m_name = name;
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
        if (obj == this)
            return true;
        if (!(obj instanceof AsdBranch))
            return false;

        AsdBranch rhs = (AsdBranch) obj;
        return new EqualsBuilder().
                append(getName(), rhs.getName()).
                isEquals();
    }
}
