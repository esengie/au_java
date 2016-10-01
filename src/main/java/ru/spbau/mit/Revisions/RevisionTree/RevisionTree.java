package ru.spbau.mit.Revisions.RevisionTree;

import com.sun.istack.internal.NotNull;
import ru.spbau.mit.Revisions.Branches.AsdBranch;
import ru.spbau.mit.Revisions.CommitNodes.CommitNode;
import ru.spbau.mit.Revisions.Exceptions.BranchAlreadyExistsException;
import ru.spbau.mit.Revisions.Exceptions.BranchDoesntExistException;
import ru.spbau.mit.Revisions.Exceptions.CommitDoesntExistException;
import ru.spbau.mit.Revisions.Exceptions.CommitNodeAlreadyExistsRuntimeException;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * The Revision tree in all its glory
 *
 * Needs to provide equals and hashcode
 */
public interface RevisionTree extends Serializable {
    @NotNull
    List<CommitNode> getLogPath();
    @NotNull
    AsdBranch getCurrentBranch();
    @NotNull
    CommitNode getHeadOfBranch(AsdBranch a_branch) throws BranchDoesntExistException;
    @NotNull
    Set<AsdBranch> getBranches();

    int getRevisionNumber();
    boolean isEarlierThanCurrent(AsdBranch a_branch) throws BranchDoesntExistException;

    void branchCreate(AsdBranch a_branch) throws BranchAlreadyExistsException;
    void branchRemove(AsdBranch a_branch);

    void commit(CommitNode a_node) throws CommitNodeAlreadyExistsRuntimeException;

    @NotNull
    CommitNode checkout(AsdBranch a_branch) throws BranchDoesntExistException;
    @NotNull
    CommitNode checkout(int a_revisionNumber) throws CommitDoesntExistException;

    void merge(AsdBranch a_branch, CommitNode merged) throws BranchDoesntExistException;
}
