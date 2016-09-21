package ru.spbau.mit.AsdCommand;

import java.io.IOException;
import java.util.List;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

@Parameters(commandDescription = "AddCommand files to the index")
public class AddCommand extends AsdCommand {
    @Parameter(description = "File patterns to add to the index")
    private List<String> patterns;

    protected AddCommand() {
        super();
    }

    @Override
    public void run() throws IOException {
        for (String s : patterns) {
            System.out.println(s);
        }
    }
}
