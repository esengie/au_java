package ru.spbau.mit.AsdCommand;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import ru.spbau.mit.AsdCommand.Exceptions.TooFewArgumentsException;
import ru.spbau.mit.Revisions.RevisionTree.RevisionTree;
import ru.spbau.mit.Staging.Staging;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;

@Parameters(commandDescription = "Rm files from the repo")
public class RmCommand implements AsdCommand {
    @Parameter(description = "Files to remove")
    private List<String> patterns;

    protected RmCommand() {
    }

    @Override
    public void run(RevisionTree tree, Staging staging, PrintStream writer) throws IOException {
        if (patterns == null)
            throw new TooFewArgumentsException("rm needs some args");
        for (String s : new HashSet<>(patterns)) {
            staging.remove(Paths.get(s));
        }
    }
}
