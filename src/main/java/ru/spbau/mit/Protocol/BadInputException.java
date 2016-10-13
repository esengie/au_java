package ru.spbau.mit.Protocol;

import java.io.IOException;

public class BadInputException extends IOException {
    public BadInputException(String s) {
        super(s);
    }
}
