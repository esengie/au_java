package ru.spbau.mit.Staging;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class RepoStatusImpl implements RepoStatus {
    private List<String> m_added;
    private List<String> m_modifiedAdded;
    private List<String> m_modifiedUnAdded;
    private List<String> m_untracked;
    private List<String> m_removed;

    RepoStatusImpl(Collection<String> added,
                   Collection<String> modifiedAdded,
                   Collection<String> modifiedUnAdded,
                   Collection<String> untracked,
                   Collection<String> removed) {
        m_added = new ArrayList<>(added);
        m_modifiedAdded = new ArrayList<>(modifiedAdded);
        m_modifiedUnAdded = new ArrayList<>(modifiedUnAdded);
        m_untracked = new ArrayList<>(untracked);
        m_removed = new ArrayList<>(removed);
    }

    @Override
    public List<String> modifiedAdded() {
        return m_modifiedAdded;
    }

    @Override
    public List<String> modifiedUnAdded() {
        return m_modifiedUnAdded;
    }

    @Override
    public List<String> untracked() {
        return m_untracked;
    }

    @Override
    public List<String> added() {
        return m_added;
    }

    @Override
    public List<String> removed() {
        return m_removed;
    }
}
