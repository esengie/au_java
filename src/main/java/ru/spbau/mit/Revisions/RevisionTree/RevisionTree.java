package ru.spbau.mit.Revisions.RevisionTree;

import com.sun.istack.internal.NotNull;
import ru.spbau.mit.Revisions.Branches.AsdBranch;
import ru.spbau.mit.Revisions.CommitNodes.CommitNode;
import ru.spbau.mit.Revisions.Exceptions.BranchDoesntExistException;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

public interface RevisionTree extends Serializable {
    @NotNull
    List<CommitNode> getLogPath();
    @NotNull
    AsdBranch getCurrentBranch();
    @NotNull
    Set<AsdBranch> getBranches();

    int getRevisionNumber();

    void branchCreate(AsdBranch a_branch);
    void branchRemove(AsdBranch a_branch);

    void commit(CommitNode a_node);

    void merge(AsdBranch a_branch, CommitNode merged) throws BranchDoesntExistException;

    boolean branchExists(AsdBranch a_branch);
}
