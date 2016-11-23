package ru.spbau.mit.Revisions.Exceptions;

import java.io.IOException;

public class BranchAlreadyExistsException extends IOException {
    public BranchAlreadyExistsException(String s) {
        super(s);
    }
}
