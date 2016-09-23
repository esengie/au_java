package ru.spbau.mit.Revisions.CommitNodes;

import com.sun.istack.internal.NotNull;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import ru.spbau.mit.Revisions.Branches.AsdBranch;

import java.io.Serializable;

/**
 * CommitNode represents a node in the Revision tree
 *
 * Comes with it's own branch, get assigned a revision number BY THE TREE
 *
 * Needs to be commited to the tree before getting any other Nodes from the Factory
 * Needs to provide equals and hashcode
 */
public interface CommitNode extends Serializable {
    @NotNull
    String getMessage();

    @NotNull
    int getRevisionNumber();

    @NotNull
    AsdBranch getBranch();

    default public int hashCoder() {
        return new HashCodeBuilder(17, 31).
                append(getBranch()).
                append(getMessage()).
                append(getRevisionNumber()).
                toHashCode();
    }

    default public boolean equalser(Object obj) {
        if (!(obj instanceof CommitNode))
            return false;
        if (obj == this)
            return true;

        CommitNode rhs = (CommitNode) obj;
        return new EqualsBuilder().
                append(getBranch(), rhs.getBranch()).
                append(getMessage(), rhs.getMessage()).
                append(getRevisionNumber(), rhs.getRevisionNumber()).
                isEquals();
    }
}
