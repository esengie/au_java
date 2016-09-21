package ru.spbau.mit.AsdCommand;

import com.beust.jcommander.Parameters;

import java.io.IOException;

@Parameters(commandDescription = "Show commit log")
public class LogRefactor extends AsdCommand {

    protected LogRefactor(){super();}

    @Override
    public void run() throws IOException {
        super.run();
    }
}
