package ru.spbau.mit.Revisions;

import com.sun.istack.internal.NotNull;

import java.util.Set;

public interface CommitNode {
    @NotNull
    String getAuthor();

    @NotNull
    String getHashCode();

    @NotNull
    AsdBranch getBranch();
}
