package ru.spbau.mit.Staging;


import java.util.List;

public interface RepoStatus {
    List<String> modified();
    List<String> untracked();
    List<String> added();
    List<String> removed();
}
