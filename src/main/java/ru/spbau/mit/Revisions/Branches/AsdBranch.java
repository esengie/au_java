package ru.spbau.mit.Revisions.Branches;

import com.sun.istack.internal.NotNull;

import java.io.Serializable;


/**
 * The branch interface
 * <p>
 * Needs to provide equals and hashcode
 */
public interface AsdBranch extends Serializable {
    @NotNull
    String getName();
}
