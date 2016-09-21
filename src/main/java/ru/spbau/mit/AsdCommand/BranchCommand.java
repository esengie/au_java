package ru.spbau.mit.AsdCommand;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

import java.io.IOException;
import java.util.List;

@Parameters(commandDescription = "AddCommand file contents to the index")
public class BranchCommand extends AsdCommand {

    @Parameter(description = "The branch names")
    private List<String> branchName;

    @Parameter(names = "-d")
    private boolean deleteMode = false;

    protected BranchCommand(){super();}

    @Override
    public void run() throws IOException {
        super.run();
    }
}
