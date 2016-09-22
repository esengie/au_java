package ru.spbau.mit.Revisions.CommitNodes;

import com.sun.istack.internal.NotNull;
import ru.spbau.mit.Revisions.Branches.AsdBranch;

import java.io.Serializable;

public interface CommitNode extends Serializable {
    @NotNull
    String getMessage();

    @NotNull
    int getRevisionNumber();

    @NotNull
    AsdBranch getBranch();
}
