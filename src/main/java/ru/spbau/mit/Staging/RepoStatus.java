package ru.spbau.mit.Staging;


import java.util.List;

/**
 * A holder for the status command
 */
public interface RepoStatus {
    List<String> modifiedAdded();
    List<String> modifiedUnAdded();
    List<String> untracked();
    List<String> added();
    List<String> removed();
}
