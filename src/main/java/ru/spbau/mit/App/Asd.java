package ru.spbau.mit.App;

import ru.spbau.mit.AsdCommand.Exceptions.NotAnAsdFolder;
import ru.spbau.mit.AsdCommand.Exceptions.SerializedTreeNotFoundError;
import ru.spbau.mit.AsdCommand.AsdCommand;
import ru.spbau.mit.AsdCommand.Exceptions.AlreadyAnAsdFolderException;
import ru.spbau.mit.AsdCommand.InitCommand;
import ru.spbau.mit.Cli.Cli;
import ru.spbau.mit.Revisions.RevisionTree.RevisionTree;
import ru.spbau.mit.Revisions.RevisionTree.RevisionTreeSerializer;
import ru.spbau.mit.Revisions.RevisionTree.RevisionTreeSerializerImpl;
import ru.spbau.mit.Staging.Staging;
import ru.spbau.mit.Staging.StagingImpl;

import static ru.spbau.mit.Paths.AsdFolderOperations.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Scanner;

public class Asd {
    private RevisionTree m_tree;
    private Staging m_staging;

    private void loadRevisionTree() throws NotAnAsdFolder {
        if (!isAnAsdFolder())
            throw new NotAnAsdFolder();
        RevisionTreeSerializer serializer = new RevisionTreeSerializerImpl();

        try {
            m_tree = serializer.deserialize(new FileInputStream(new File(getSerializedTreePath())));
        } catch (IOException e) {
            throw new SerializedTreeNotFoundError();
        }
    }

    private void saveRevisionTree() throws NotAnAsdFolder {
        if (!isAnAsdFolder())
            throw new NotAnAsdFolder();

        RevisionTreeSerializer serializer = new RevisionTreeSerializerImpl();

        File out = new File(getSerializedTreePath());
        try {
            out.createNewFile();
            serializer.serialize(m_tree, new FileOutputStream(out));
        } catch (IOException e) {
            throw new IllegalStateException("Unknown error during saving the tree");
        }

    }

    public static void main(String... argv) {
        Scanner scanner = new Scanner(System.in);
        Asd asd = new Asd();
        boolean loaded = false;
        while (scanner.hasNextLine()) {
            try {
                String line = scanner.nextLine();
                if (line == null) continue;
                AsdCommand cmd = Cli.parseAndDispatch(line.split("\\s"));
                if (!isAnAsdFolder() && !InitCommand.class.isInstance(cmd))
                    throw new NotAnAsdFolder();

                if (!loaded && isAnAsdFolder()) {
                    asd.loadRevisionTree();
                    asd.m_staging = new StagingImpl(getRoot());
                    loaded = true;
                }
                cmd.run(asd.m_tree, asd.m_staging);

            } catch (AlreadyAnAsdFolderException e) {
                System.out.println("Can't init an asd folder");
            } catch (NotAnAsdFolder e) {
                System.out.println("Not an asd folder you should init first");
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }

}
