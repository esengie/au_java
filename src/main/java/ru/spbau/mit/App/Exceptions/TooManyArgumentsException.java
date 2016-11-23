package ru.spbau.mit.App.Exceptions;

import java.io.IOException;

public class TooManyArgumentsException extends IOException {
    public TooManyArgumentsException(String s) {
        super(s);
    }
}
