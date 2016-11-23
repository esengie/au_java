package ru.spbau.mit.App.Exceptions;

import java.io.IOException;

public class RevisionTreeLoadException extends IOException {
    public RevisionTreeLoadException(String s) {
        super(s);
    }
}
