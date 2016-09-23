package ru.spbau.mit.Revisions.Branches;

import com.sun.istack.internal.NotNull;

import java.io.Serializable;

public interface AsdBranch extends Serializable {
    @NotNull
    String getName();
}
