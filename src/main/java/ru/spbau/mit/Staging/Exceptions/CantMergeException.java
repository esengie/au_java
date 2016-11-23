package ru.spbau.mit.Staging.Exceptions;

import java.io.IOException;

public class CantMergeException extends IOException {
    public CantMergeException(String s) {
        super(s);
    }
}
