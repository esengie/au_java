package ru.spbau.mit.Revisions;

import com.sun.istack.internal.NotNull;

public interface CommitNode {
    @NotNull
    String getMessage();

    @NotNull
    int getRevisionNumber();

    @NotNull
    AsdBranch getBranch();
}
