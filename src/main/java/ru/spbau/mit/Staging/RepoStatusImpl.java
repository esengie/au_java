package ru.spbau.mit.Staging;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class RepoStatusImpl implements RepoStatus {
    private List<String> m_modified;
    private List<String> m_untracked;
    private List<String> m_removed;
    private List<String> m_added;

    RepoStatusImpl(Collection<String> added,
                   Collection<String> modified,
                   Collection<String> untracked,
                   Collection<String> removed) {
        m_added = new ArrayList<>(added);
        m_modified = new ArrayList<>(modified);
        m_untracked = new ArrayList<>(untracked);
        m_removed = new ArrayList<>(removed);
    }

    @Override
    public List<String> modified() {
        return m_modified;
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
