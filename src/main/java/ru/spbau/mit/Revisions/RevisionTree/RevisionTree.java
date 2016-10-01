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
 * <p>
 * Needs to provide equals and hashcode
 */
public interface RevisionTree extends Serializable {
    @NotNull
    List<CommitNode> getLogPath();

    @NotNull
    AsdBranch getCurrentBranch();

    @NotNull
    CommitNode getHeadOfBranch(AsdBranch branch) throws BranchDoesntExistException;

    @NotNull
    Set<AsdBranch> getBranches();

    int getRevisionNumber();

    boolean isEarlierThanCurrent(AsdBranch branch) throws BranchDoesntExistException;

    void branchCreate(AsdBranch branch) throws BranchAlreadyExistsException;

    void branchRemove(AsdBranch branch);

    void commit(CommitNode node) throws CommitNodeAlreadyExistsRuntimeException;

    @NotNull
    CommitNode checkout(AsdBranch branch) throws BranchDoesntExistException;

    @NotNull
    CommitNode checkout(int revisionNumber) throws CommitDoesntExistException;

    void merge(AsdBranch branch, CommitNode merged) throws BranchDoesntExistException;
}
