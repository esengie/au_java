package ru.spbau.mit.Revisions.Exceptions;

public class IncorrectFileRuntimeException extends RuntimeException {
    public IncorrectFileRuntimeException(String s, Throwable throwable) {
        super(s, throwable);
    }
}
