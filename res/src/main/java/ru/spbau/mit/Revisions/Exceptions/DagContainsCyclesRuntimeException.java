package ru.spbau.mit.Revisions.Exceptions;

public class DagContainsCyclesRuntimeException extends IllegalStateException {
    public DagContainsCyclesRuntimeException(String s, Throwable throwable) {
        super(s, throwable);
    }
}
