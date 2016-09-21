package ru.spbau.mit.AsdCommand;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

import java.io.IOException;
import java.util.List;

@Parameters(commandDescription = "Merge a branch to this one")
public class MergeCommand extends AsdCommand {
    @Parameter(description = "The branch name")
    private List<String> branchName;

    protected MergeCommand(){super();}

    @Override
    public void run() throws IOException {
        super.run();
    }
}
