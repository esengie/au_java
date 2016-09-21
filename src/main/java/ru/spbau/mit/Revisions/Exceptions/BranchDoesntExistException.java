package ru.spbau.mit.Revisions.Exceptions;

public class BranchDoesntExistException extends Exception {
    public BranchDoesntExistException(String s) {
        super(s);
    }
}
