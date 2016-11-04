package ru.spbau.mit.AsdCommand.Exceptions;

import java.io.IOException;

public class SerializedStateNotFoundException extends IOException {
    public SerializedStateNotFoundException(String s) {
        super(s);
    }
}
