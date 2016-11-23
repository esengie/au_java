package ru.spbau.mit.Revisions.Exceptions;

public class BranchingRuntimeException extends RuntimeException {
    public BranchingRuntimeException(Throwable throwable) {
        super(throwable);
    }
}
