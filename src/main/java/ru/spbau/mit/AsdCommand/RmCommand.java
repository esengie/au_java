package ru.spbau.mit.AsdCommand;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import ru.spbau.mit.AsdCommand.Exceptions.TooLittleArgumentsException;
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

    /**
     * Removes the files to the latest known state
     *
     * @param a_tree    A Revision tree
     * @param a_staging The Staging and disk class
     * @param a_writer  OutPutStream
     * @throws IOException may throw depending on staging class implementation
     */
    @Override
    public void run(RevisionTree a_tree, Staging a_staging, PrintStream a_writer) throws IOException {
        if (patterns == null)
            throw new TooLittleArgumentsException("rm needs some args");
        for (String s : new HashSet<>(patterns)) {
            a_staging.remove(Paths.get(s));
        }
    }
}
