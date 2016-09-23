package ru.spbau.mit.AsdCommand.Exceptions;

import java.io.IOException;

public class TooLittleArgumentsException extends IOException {
    public TooLittleArgumentsException(String s) {
        super(s);
    }
}
