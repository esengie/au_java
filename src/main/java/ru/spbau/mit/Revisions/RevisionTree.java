package ru.spbau.mit.Revisions;

import com.sun.istack.internal.NotNull;
import ru.spbau.mit.Revisions.Exceptions.BranchDoesntExistException;

import java.util.List;
import java.util.Set;

public interface RevisionTree {
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
