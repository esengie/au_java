package ru.spbau.mit.Staging.Exceptions;


public class FileShouldExistError extends RuntimeException {
    public FileShouldExistError(String s) {
        super(s);
    }
}
