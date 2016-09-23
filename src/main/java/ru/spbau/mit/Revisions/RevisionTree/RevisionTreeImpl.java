package ru.spbau.mit.Revisions.RevisionTree;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.jgrapht.experimental.dag.DirectedAcyclicGraph;
import org.jgrapht.graph.DefaultEdge;
import ru.spbau.mit.Revisions.Branches.AsdBranch;
import ru.spbau.mit.Revisions.Branches.AsdBranchFactory;
import ru.spbau.mit.Revisions.CommitNodes.CommitNode;
import ru.spbau.mit.Revisions.Exceptions.*;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.*;

public class RevisionTreeImpl implements RevisionTree {
    public RevisionTreeImpl() {
        class CNode implements CommitNode {
            @Override
            public String getMessage() {
                return "Init Commit contains everything before init";
            }

            @Override
            public int getRevisionNumber() {
                return 0;
            }

            @Override
            public AsdBranch getBranch() {
                return AsdBranchFactory.createBranch("master");
            }

            @Override
            public int hashCode() {
                return hashCoder();
            }

            @Override
            public boolean equals(Object obj) {
                return equalser(obj);
            }
        }

        CommitNode c = new CNode();
        m_graph.addVertex(c);
        setHeadCommitForBranch(m_currentBranch, c);
    }

    private DirectedAcyclicGraph<CommitNode, DefaultEdge> m_graph
            = new DirectedAcyclicGraph<>(DefaultEdge.class);

    private AsdBranch m_currentBranch = AsdBranchFactory.createBranch("master");

    private Map<AsdBranch, CommitNode> m_branchToHeadCommitMap = new HashMap<>();

    @Override
    public List<CommitNode> getLogPath() {
        CommitNode node = getHeadCommitForBranch(m_currentBranch);
        AsdBranch branch = node.getBranch();

        List<CommitNode> path = new ArrayList<>();
        path.add(node);

        Set<CommitNode> ancSet;
        while ((ancSet = m_graph.getAncestors(m_graph, node)).size() > 0) {
            CommitNode ancestor = null;
            if (ancSet.size() > 2)
                throw new RevisionTreeAncestorsRuntimeError("Error: >2 ancestors");
            if (ancSet.size() == 1){
                ancestor = ancSet.iterator().next();
            } else {
                for (CommitNode a : ancSet) {
                    if (a.getBranch().equals(branch)) {
                        if (ancestor != null)
                            throw new RevisionTreeAncestorsRuntimeError("Error: passed >2 ancestors check, we don't handle weird merging cases yet");
                        ancestor = a;
                    }
                }
            }

            if (ancestor == null)
                throw new RevisionTreeAncestorsRuntimeError("Error: no ancestors");
            path.add(ancestor);
            node = ancestor;
            branch = ancestor.getBranch();
        }

        return path;
    }

    @Override
    public AsdBranch getCurrentBranch() {
        return m_currentBranch;
    }

    @Override
    public Set<AsdBranch> getBranches() {
        return m_branchToHeadCommitMap.keySet();
    }

    @Override
    public int getRevisionNumber() {
        return m_graph.vertexSet().size();
    }

    @Override
    public void branchCreate(AsdBranch a_branch) throws BranchAlreadyExistsException {
        if (branchExists(a_branch))
            throw new BranchAlreadyExistsException(a_branch.getName());
        setHeadCommitForBranch(a_branch, getHeadCommitForBranch(m_currentBranch));
    }

    @Override
    public void branchRemove(AsdBranch a_branch) {
        throw new NotImplementedException();
    }

    @Override
    public void commit(CommitNode a_node) {
        if (a_node.getRevisionNumber() != getRevisionNumber())
            throw new CommitNodeAlreadyExistsError();
        CommitNode current = getHeadCommitForBranch(m_currentBranch);
        addDagEdge(current, a_node);
        setHeadCommitForBranch(m_currentBranch, a_node);
    }

    @Override
    public CommitNode checkout(AsdBranch a_branch) throws BranchDoesntExistException {
        if (!branchExists(a_branch))
            throw new BranchDoesntExistException(a_branch.getName());
        m_currentBranch = a_branch;
        return getHeadCommitForBranch(a_branch);
    }

    @Override
    public CommitNode checkout(int a_revisionNumber) throws CommitDoesntExistException {
        if (getRevisionNumber() <= a_revisionNumber)
            throw new CommitDoesntExistException(String.valueOf(a_revisionNumber));

        CommitNode retVal = null;
        for (CommitNode c : m_graph) {
            if (c.getRevisionNumber() == a_revisionNumber) {
                retVal = c;
                break;
            }
        }

        if (retVal == null)
            throw new IllegalStateException("Couldn't find a commit node by its revision number");

        m_currentBranch = retVal.getBranch();
        setHeadCommitForBranch(m_currentBranch, retVal);

        return retVal;
    }

    private void addDagEdge(CommitNode a_from, CommitNode a_to) {
        try {
            if (a_to.getRevisionNumber() >= m_graph.vertexSet().size())
                m_graph.addVertex(a_to);
            m_graph.addDagEdge(a_from, a_to);
        } catch (DirectedAcyclicGraph.CycleFoundException e) {
            throw new DagContainsCyclesRuntimeException("Error: Dag contains cycles, program error", e);
        }
    }

    /**
     * Performs a three-way merge, always
     *
     * @param a_branch a branchCreate to merge into the current one
     */
    @Override
    public void merge(AsdBranch a_branch, CommitNode a_merged) throws BranchDoesntExistException {
        if (!branchExists(a_branch))
            throw new BranchDoesntExistException("No such branchCreate: " + a_branch.getName());

        CommitNode mergee = getHeadCommitForBranch(a_branch);
        CommitNode current = getHeadCommitForBranch(m_currentBranch);

        addDagEdge(mergee, a_merged);
        addDagEdge(current, a_merged);


        setHeadCommitForBranch(a_branch, a_merged);
        setHeadCommitForBranch(m_currentBranch, a_merged);
    }

    @Override
    public CommitNode getHeadCommitOfBranch(AsdBranch a_branch) throws BranchDoesntExistException {
        CommitNode retVal = getHeadCommitForBranch(a_branch);
        if (retVal == null)
            throw new BranchDoesntExistException(a_branch.getName());
        return retVal;
    }

    private CommitNode getHeadCommitForBranch(AsdBranch a_branch) {
        return m_branchToHeadCommitMap.get(a_branch);
    }

    private void setHeadCommitForBranch(AsdBranch a_branch, CommitNode a_node) {
        m_branchToHeadCommitMap.put(a_branch, a_node);
    }

    private boolean branchExists(AsdBranch a_branch) {
        return m_branchToHeadCommitMap.containsKey(a_branch);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 31). // two randomly chosen prime numbers
                append(m_graph.vertexSet()).
                append(m_branchToHeadCommitMap).
                append(m_currentBranch).
                toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof RevisionTreeImpl))
            return false;
        if (obj == this)
            return true;

        RevisionTreeImpl rhs = (RevisionTreeImpl) obj;
        return new EqualsBuilder().
                append(m_graph.vertexSet(), rhs.m_graph.vertexSet()).
                append(m_branchToHeadCommitMap, rhs.m_branchToHeadCommitMap).
                append(m_currentBranch, rhs.m_currentBranch).
                isEquals();
    }
}
