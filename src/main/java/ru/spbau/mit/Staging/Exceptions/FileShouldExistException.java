package ru.spbau.mit.Staging.Exceptions;


import java.io.IOException;

public class FileShouldExistException extends IOException {
    public FileShouldExistException(String s) {
        super(s);
    }
}
