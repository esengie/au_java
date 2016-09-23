package ru.spbau.mit.Revisions.Exceptions;

import java.io.IOException;

public class CommitDoesntExistException extends IOException {
    public CommitDoesntExistException(String s) {
        super(s);
    }
}
