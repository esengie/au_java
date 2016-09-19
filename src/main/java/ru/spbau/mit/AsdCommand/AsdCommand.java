package ru.spbau.mit.AsdCommand;

import java.io.IOException;
import java.util.List;

abstract public class AsdCommand {
    private List<String> m_args;

    protected AsdCommand(List<String> a_args){
        m_args = a_args;
    }

    public void run() throws IOException {
        throw new NoSuchMethodError("Not implemented");
    }
}
