package ru.spbau.mit.AsdCommand.Exceptions;

import java.io.IOException;

public class TooFewArgumentsException extends IOException {
    public TooFewArgumentsException(String s) {
        super(s);
    }
}
