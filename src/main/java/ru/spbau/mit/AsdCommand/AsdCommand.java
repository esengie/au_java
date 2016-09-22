package ru.spbau.mit.AsdCommand;

import ru.spbau.mit.AsdCommand.Exceptions.AlreadyAnAsdFolderException;

import java.io.IOException;

abstract public class AsdCommand {
    AsdCommand(){
    }

    public void run() throws IOException, AlreadyAnAsdFolderException {
        throw new NoSuchMethodError("Not implemented");
    }
}
