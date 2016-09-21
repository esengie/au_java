package ru.spbau.mit.AsdCommand;

import com.beust.jcommander.Parameters;
import ru.spbau.mit.Revisions.RevisionTree;
import ru.spbau.mit.Revisions.RevisionTreeImpl;
import ru.spbau.mit.Staging.Staging;
import ru.spbau.mit.Staging.StagingImpl;

import java.io.IOException;

@Parameters(commandDescription = "Initialise the repo (if uninitialised)")
public class InitCommand extends AsdCommand {

    protected InitCommand(){super();}

    @Override
    public void run() throws IOException {
//        if (dir doesnt exist)
        Staging staging = new StagingImpl();
        RevisionTree revTree = new RevisionTreeImpl();
    }
}