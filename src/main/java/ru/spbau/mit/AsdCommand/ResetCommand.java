package ru.spbau.mit.AsdCommand;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import ru.spbau.mit.AsdCommand.Exceptions.TooLittleArgumentsException;
import ru.spbau.mit.Revisions.RevisionTree.RevisionTree;
import ru.spbau.mit.Staging.Staging;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.HashSet;
import java.util.List;

@Parameters(commandDescription = "Reset files from the repo")
public class ResetCommand implements AsdCommand {
    @Parameter(description = "Files to reset")
    private List<String> files;

    protected ResetCommand() {
    }

    /**
     * Resets the files to the latest known state
     *
     * @param a_tree    A Revision tree
     * @param a_staging The Staging and disk class
     * @param a_writer  OutPutStream
     * @throws IOException may throw depending on staging class implementation
     */
    @Override
    public void run(RevisionTree a_tree, Staging a_staging, PrintStream a_writer) throws IOException {
        if (files == null)
            throw new TooLittleArgumentsException("reset needs some args");
        for (String s : new HashSet<>(files)) {
            a_staging.reset(new File(s).getAbsoluteFile().toPath());
        }
    }
}
