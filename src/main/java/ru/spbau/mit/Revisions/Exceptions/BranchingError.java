package ru.spbau.mit.Revisions.Exceptions;

public class BranchingError extends RuntimeException {
    public BranchingError(Throwable throwable) {
        super(throwable);
    }
}
