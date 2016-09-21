package ru.spbau.mit.AsdCommand;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

import java.io.IOException;
import java.util.List;

@Parameters(commandDescription = "CheckoutCommand branches")
public class CheckoutCommand extends AsdCommand {

    @Parameter(description = "BranchCommand name")
    private List<String> branches;

    protected CheckoutCommand(){super();}

    @Override
    public void run() throws IOException {
        super.run();
    }
}
