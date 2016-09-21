package ru.spbau.mit.AsdCommand;

import com.beust.jcommander.Parameters;

import java.io.IOException;

@Parameters(commandDescription = "Show commit log")
public class LogCommand extends AsdCommand {

    protected LogCommand(){super();}

    @Override
    public void run() throws IOException {
        super.run();
    }
}
