package ru.spbau.mit.Communication;

import java.io.IOException;

public class BadInputException extends IOException {
    public BadInputException(String s) {
        super(s);
    }
}
