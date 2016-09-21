package ru.spbau.mit.AsdCommand;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

import java.io.IOException;
import java.util.List;

@Parameters(commandDescription = "MergeRefactor a branch to this one")
public class MergeRefactor extends AsdCommand {
    @Parameter(description = "The branch names")
    private List<String> branchName;

    protected MergeRefactor(){super();}

    @Override
    public void run() throws IOException {
        super.run();
    }
}
