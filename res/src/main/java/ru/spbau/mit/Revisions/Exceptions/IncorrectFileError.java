package ru.spbau.mit.Revisions.Exceptions;

/**
 * Created by esengie on 9/22/16.
 */
public class IncorrectFileError extends RuntimeException {
    public IncorrectFileError(String s, Throwable throwable) {
        super(s, throwable);
    }
}
