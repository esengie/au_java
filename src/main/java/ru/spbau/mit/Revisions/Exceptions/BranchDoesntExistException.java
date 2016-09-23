package ru.spbau.mit.Revisions.Exceptions;

import java.io.IOException;

public class BranchDoesntExistException extends IOException {
    public BranchDoesntExistException(String s) {
        super(s);
    }
}
