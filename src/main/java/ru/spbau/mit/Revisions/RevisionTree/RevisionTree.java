package ru.spbau.mit.Revisions.RevisionTree;

import com.sun.istack.internal.NotNull;
import ru.spbau.mit.Revisions.Branches.AsdBranch;
import ru.spbau.mit.Revisions.CommitNodes.CommitNode;
import ru.spbau.mit.Revisions.Exceptions.BranchAlreadyExistsException;
import ru.spbau.mit.Revisions.Exceptions.BranchDoesntExistException;
import ru.spbau.mit.Revisions.Exceptions.CommitDoesntExistException;
import ru.spbau.mit.Revisions.Exceptions.CommitNodeAlreadyExistsError;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

public interface RevisionTree extends Serializable {
    @NotNull
    List<CommitNode> getLogPath();
    @NotNull
    AsdBranch getCurrentBranch();
    @NotNull
    CommitNode getHeadCommitOfBranch(AsdBranch a_branch) throws BranchDoesntExistException;
    @NotNull
    Set<AsdBranch> getBranches();

    int getRevisionNumber();

    void branchCreate(AsdBranch a_branch) throws BranchAlreadyExistsException;
    void branchRemove(AsdBranch a_branch);

    void commit(CommitNode a_node) throws CommitNodeAlreadyExistsError;

    @NotNull
    CommitNode checkout(AsdBranch a_branch) throws BranchDoesntExistException;
    @NotNull
    CommitNode checkout(int a_revisionNumber) throws CommitDoesntExistException;

    void merge(AsdBranch a_branch, CommitNode merged) throws BranchDoesntExistException;
}
