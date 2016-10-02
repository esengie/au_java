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

    /**
     * Check if the branch's commit is in the log path of the current one
     *
     * @param branch branch to check
     * @return is it?
     * @throws BranchDoesntExistException if it doesn't
     */
    boolean isEarlierThanCurrent(AsdBranch branch) throws BranchDoesntExistException;

    /**
     * Creates a branch pointing to the current commit (branch is just a tag)
     *
     * @param branch branch to create in a tree
     * @throws BranchAlreadyExistsException if it already exists
     */
    void branchCreate(AsdBranch branch) throws BranchAlreadyExistsException;

    void branchRemove(AsdBranch branch);

    /**
     * Creates a commit node in the DAG and attaches to the current node
     * Current node is "node" and the branch tag gets updated
     *
     * @param node commit containg the info
     * @throws CommitNodeAlreadyExistsRuntimeException if it already exists
     */
    void commit(CommitNode node) throws CommitNodeAlreadyExistsRuntimeException;

    /**
     * Checks out the latest node of the branch
     *
     * @param branch branch to checkout
     * @return the node
     * @throws BranchDoesntExistException if it doesn't
     */
    @NotNull
    CommitNode checkout(AsdBranch branch) throws BranchDoesntExistException;

    /**
     * Checks out the commit
     *
     * @param revisionNumber commit number to checkout
     * @return the node
     * @throws CommitDoesntExistException if it doesn't
     */
    @NotNull
    CommitNode checkout(int revisionNumber) throws CommitDoesntExistException;

    /**
     * Performs a "history forgetting" merge, always
     * <p>
     * Except when the merged branch is in the log path, then we do nothing.
     * <p>
     * Example of "forgetting":
     * A->B->E
     * |
     * C->D->         <- if we have two paths ACD and ABE,
     * <p>
     * and merge D to E we get the log ABEF
     * if we merge E to D we get the log ACDF.
     * <p>
     * So we "forget" the commits,
     * but keep the changes (we forget them from outside only).
     * Any suggestions how to fix this are welcome
     *
     * @param branch a branchCreate to merge into the current one
     */
    void merge(AsdBranch branch, CommitNode merged) throws BranchDoesntExistException;
}
