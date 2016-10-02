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
    /**
     * Default zero node, because I decided to have a zero commit.
     * Also this means staging has to provide a zero commit - but this is high-level
     * and would be in a project description or something
     */
    static class HeadNode implements CommitNode {
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

    public RevisionTreeImpl() {
        CommitNode c = new HeadNode();
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

        Set<CommitNode> ancestorSet;
        while ((ancestorSet = directParentsOf(node)).size() > 0) {
            CommitNode ancestor = null;
            if (ancestorSet.size() > 2)
                throw new RevisionTreeAncestorsRuntimeException("Error: >2 ancestors");
            if (ancestorSet.size() == 1) {
                ancestor = ancestorSet.iterator().next();
            } else {
                for (CommitNode a : ancestorSet) {
                    if (a.getBranch().equals(branch)) {
                        if (ancestor != null)
                            throw new RevisionTreeAncestorsRuntimeException("Error: passed >2 ancestors check, we don't handle weird merging cases yet");
                        ancestor = a;
                    }
                }
            }

            if (ancestor == null)
                throw new RevisionTreeAncestorsRuntimeException("Error: no ancestors");
            path.add(ancestor);
            node = ancestor;
            branch = ancestor.getBranch();
        }

        return path;
    }

    private Set<CommitNode> directParentsOf(CommitNode node) {
        Set<DefaultEdge> incomingEdges = m_graph.incomingEdgesOf(node);
        Set<CommitNode> retVal = new HashSet<>();
        for (DefaultEdge e : incomingEdges){
            retVal.add(m_graph.getEdgeSource(e));
        }
        return retVal;
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
    public boolean isEarlierThanCurrent(AsdBranch branch) throws BranchDoesntExistException {
        CommitNode c = getHeadOfBranch(branch);
        Set<CommitNode> set = new HashSet<>(getLogPath());
        return set.contains(c);
    }

    @Override
    public void branchCreate(AsdBranch branch) throws BranchAlreadyExistsException {
        if (branchExists(branch))
            throw new BranchAlreadyExistsException(branch.getName());
        setHeadCommitForBranch(branch, getHeadCommitForBranch(m_currentBranch));
    }

    @Override
    public void branchRemove(AsdBranch branch) {
        throw new NotImplementedException();
    }

    @Override
    public void commit(CommitNode node) {
        if (node.getRevisionNumber() != getRevisionNumber())
            throw new CommitNodeAlreadyExistsRuntimeException();
        CommitNode current = getHeadCommitForBranch(m_currentBranch);
        addDagEdge(current, node);
        setHeadCommitForBranch(m_currentBranch, node);
    }

    @Override
    public CommitNode checkout(AsdBranch branch) throws BranchDoesntExistException {
        if (!branchExists(branch))
            throw new BranchDoesntExistException(branch.getName());
        m_currentBranch = branch;
        return getHeadCommitForBranch(branch);
    }

    @Override
    public CommitNode checkout(int revisionNumber) throws CommitDoesntExistException {
        if (getRevisionNumber() <= revisionNumber)
            throw new CommitDoesntExistException(String.valueOf(revisionNumber));

        CommitNode retVal = null;
        for (CommitNode c : m_graph) {
            if (c.getRevisionNumber() == revisionNumber) {
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

    private void addDagEdge(CommitNode from, CommitNode to) {
        try {
            if (to.getRevisionNumber() >= m_graph.vertexSet().size())
                m_graph.addVertex(to);
            m_graph.addDagEdge(from, to);
        } catch (DirectedAcyclicGraph.CycleFoundException e) {
            throw new DagContainsCyclesRuntimeException("Error: Dag contains cycles, program error", e);
        }
    }

    @Override
    public void merge(AsdBranch branch, CommitNode merged) throws BranchDoesntExistException {
        if (!branchExists(branch))
            throw new BranchDoesntExistException("No such branchCreate: " + branch.getName());

        CommitNode mergee = getHeadCommitForBranch(branch);
        CommitNode current = getHeadCommitForBranch(m_currentBranch);

        addDagEdge(mergee, merged);
        addDagEdge(current, merged);


        setHeadCommitForBranch(branch, merged);
        setHeadCommitForBranch(m_currentBranch, merged);
    }

    @Override
    public CommitNode getHeadOfBranch(AsdBranch branch) throws BranchDoesntExistException {
        CommitNode retVal = getHeadCommitForBranch(branch);
        if (retVal == null)
            throw new BranchDoesntExistException(branch.getName());
        return retVal;
    }

    private CommitNode getHeadCommitForBranch(AsdBranch branch) {
        return m_branchToHeadCommitMap.get(branch);
    }

    private void setHeadCommitForBranch(AsdBranch branch, CommitNode node) {
        m_branchToHeadCommitMap.put(branch, node);
    }

    private boolean branchExists(AsdBranch branch) {
        return m_branchToHeadCommitMap.containsKey(branch);
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
