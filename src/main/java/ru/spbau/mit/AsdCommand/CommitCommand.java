package ru.spbau.mit.AsdCommand;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

import java.io.IOException;
import java.util.List;

@Parameters(separators = "=", commandDescription = "Record changes to the repository")
public class CommitCommand extends AsdCommand {
    @Parameter(description = "The list of files to commit")
    private List<String> files;

    protected CommitCommand(){super();}

    @Parameter(names = "-m", description = "Commit message", required = true)
    private String message = "";

    @Override
    public void run() throws IOException {
//        super.run();
    }
}
