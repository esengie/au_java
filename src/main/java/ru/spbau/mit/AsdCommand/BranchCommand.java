package ru.spbau.mit.AsdCommand;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

import java.io.IOException;
import java.util.List;

@Parameters(commandDescription = "Branch managements")
public class BranchCommand extends AsdCommand {

    @Parameter(description = "The branch name")
    private List<String> branchName;

    @Parameter(names = "-d", description = "delete branch")
    private boolean deleteMode = false;

    protected BranchCommand(){super();}

    @Override
    public void run() throws IOException {
        super.run();
    }
}
