package ru.spbau.mit.AsdCommand;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

import java.io.IOException;
import java.util.List;

@Parameters(commandDescription = "Checkout a branch or a commit")
public class CheckoutCommand extends AsdCommand {

    @Parameter(description = "Branch name or revision")
    private List<String> branch;

    protected CheckoutCommand(){super();}

    @Override
    public void run() throws IOException {
        super.run();
    }
}
