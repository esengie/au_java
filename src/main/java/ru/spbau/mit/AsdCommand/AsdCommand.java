package ru.spbau.mit.AsdCommand;

import java.io.IOException;

abstract public class AsdCommand {
    AsdCommand(){
    }

    public void run() throws IOException {
        throw new NoSuchMethodError("Not implemented");
    }
}
