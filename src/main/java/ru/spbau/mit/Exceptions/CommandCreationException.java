package ru.spbau.mit.Exceptions;

public class CommandCreationException extends Exception {
    public CommandCreationException(String name, Throwable cause) {
        super("Cannot create command " + name, cause);
    }
}
