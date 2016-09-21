package ru.spbau.mit.Revisions;

import org.jgrapht.experimental.dag.DirectedAcyclicGraph;
import org.jgrapht.graph.DefaultEdge;
import ru.spbau.mit.Revisions.Exceptions.BranchDoesntExistException;
import ru.spbau.mit.Revisions.Exceptions.DagContainsCyclesRuntimeException;
import ru.spbau.mit.Revisions.Exceptions.RevisionTreeAncestorsRuntimeError;

import java.util.*;

public class RevisionTreeImpl implements RevisionTree {
    private DirectedAcyclicGraph<CommitNode, DefaultEdge> m_graph
            = new DirectedAcyclicGraph<>(DefaultEdge.class);

    private AsdBranch m_currentBranch = new AsdBranchImpl("");

    private Map<AsdBranch, CommitNode> m_branchToHeadCommitMap = new HashMap<>();

    @Override
    public List<CommitNode> getLogPath() {
        CommitNode node = getHeadCommitForBranch(m_currentBranch);
        AsdBranch branch = m_currentBranch;

        List<CommitNode> path = new ArrayList<>();
        path.add(node);

        Set<CommitNode> ancSet = null;
        while ((ancSet = m_graph.getAncestors(m_graph, node)).size() > 0){
            CommitNode ancestor = null;
            if (ancSet.size() > 2)
                throw new RevisionTreeAncestorsRuntimeError("Error: >2 ancestors");

            for (CommitNode a : ancSet){
                if (a.getBranch().getName().equals(branch.getName())) {
                    if (ancestor != null)
                        throw new RevisionTreeAncestorsRuntimeError("Error: passed >2 ancestors check, we don't handle weird merging cases yet");
                    ancestor = a;
                }
            }

            if (ancestor == null)
                throw new RevisionTreeAncestorsRuntimeError("Error: no ancestors");
            path.add(ancestor);
            node = ancestor;
            branch = ancestor.getBranch();
        }

        Collections.reverse(path);
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
    public void branch(AsdBranch a_branch) {

    }

    @Override
    public void commit(CommitNode a_node) {
        CommitNode current = getHeadCommitForBranch(m_currentBranch);
        addDagEdge(current, a_node);

        setHeadCommitForBranch(m_currentBranch, a_node);
    }

    private void addDagEdge(CommitNode a_from, CommitNode a_to){
        try {
            m_graph.addDagEdge(a_from, a_to);
        } catch (DirectedAcyclicGraph.CycleFoundException e) {
            throw new DagContainsCyclesRuntimeException("Error: Dag contains cycles, program error", e);
        }
    }

    /**
     * Performs a three-way merge, always
     *
     * @param a_branch a branch to merge into the current one
     */
    @Override
    public void merge(AsdBranch a_branch, CommitNode merged) throws BranchDoesntExistException {
        if (!branchExists(a_branch))
            throw new BranchDoesntExistException("No such branch: " + a_branch.getName());

        CommitNode mergee = getHeadCommitForBranch(a_branch);
        CommitNode current = getHeadCommitForBranch(m_currentBranch);

        addDagEdge(mergee, merged);
        addDagEdge(current, merged);

        setHeadCommitForBranch(a_branch, merged);
        setHeadCommitForBranch(m_currentBranch, merged);
    }

    private CommitNode getHeadCommitForBranch(AsdBranch a_branch){
        return m_branchToHeadCommitMap.get(a_branch);
    }
    private void setHeadCommitForBranch(AsdBranch a_branch, CommitNode a_node){
        m_branchToHeadCommitMap.put(a_branch, a_node);
    }

    @Override
    public boolean branchExists(AsdBranch a_branch){
        return m_branchToHeadCommitMap.containsKey(a_branch);
    }
}
