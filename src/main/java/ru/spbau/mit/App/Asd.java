package ru.spbau.mit.App;

import com.beust.jcommander.ParameterException;
import ru.spbau.mit.App.Exceptions.RevisionTreeLoadError;
import ru.spbau.mit.AsdCommand.Exceptions.NotAnAsdFolder;
import ru.spbau.mit.AsdCommand.Exceptions.SerializedTreeNotFoundError;
import ru.spbau.mit.AsdCommand.AsdCommand;
import ru.spbau.mit.AsdCommand.Exceptions.AlreadyAnAsdFolderException;
import ru.spbau.mit.AsdCommand.InitCommand;
import ru.spbau.mit.Cli.Cli;
import ru.spbau.mit.Revisions.RevisionTree.RevisionTree;
import ru.spbau.mit.Revisions.RevisionTree.RevisionTreeImpl;
import ru.spbau.mit.Revisions.RevisionTree.RevisionTreeSerializer;
import ru.spbau.mit.Revisions.RevisionTree.RevisionTreeSerializerImpl;
import ru.spbau.mit.Staging.Staging;
import ru.spbau.mit.Staging.StagingImpl;

import static ru.spbau.mit.Paths.AsdFolderOperations.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

        if (m_tree == null)
            throw new RevisionTreeLoadError();
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

    private void initCommand() throws IOException {
        if (isAnAsdFolder()) {
            throw new AlreadyAnAsdFolderException();
        }
        m_staging = new StagingImpl(Paths.get("").toFile().getAbsoluteFile().toPath());
        m_tree = new RevisionTreeImpl();
    }

    public static void main(String... argv) {
        Scanner scanner = new Scanner(System.in);
        Asd asd = new Asd();
        boolean loaded = false;
        while (scanner.hasNextLine()) {
            try {
                String line = scanner.nextLine();
                if (line == null || line.trim().equals("")) continue;

                AsdCommand cmd = Cli.parseAndDispatch(splitOnWhiteSpace(line));

                if (!isAnAsdFolder() && !InitCommand.class.isInstance(cmd))
                    throw new NotAnAsdFolder();

                if (!loaded && isAnAsdFolder()) {
                    asd.loadRevisionTree();
                    asd.m_staging = new StagingImpl(getRoot());
                    loaded = true;
                }

                if (InitCommand.class.isInstance(cmd)) {
                    asd.initCommand();
                    loaded = true;
                } else
                    cmd.run(asd.m_tree, asd.m_staging, System.out);

            } catch (AlreadyAnAsdFolderException e) {
                System.out.println("Can't init an asd folder");
            } catch (NotAnAsdFolder e) {
                System.out.println("Not an asd folder you should init first");
            } catch (ParameterException e) {
                System.out.println(e.getMessage());
                Cli.getParser().usage();
            } catch (IOException e) {
                System.out.println(e);
            }
        }

        try {
            asd.saveRevisionTree();
        } catch (NotAnAsdFolder notAnAsdFolder) {
            notAnAsdFolder.printStackTrace();
        }
    }

    private static String[] splitOnWhiteSpace(String a_string){
        List<String> matchList = new ArrayList<String>();
        Pattern regex = Pattern.compile("[^\\s\"']+|\"[^\"]*\"|'[^']*'");
        Matcher regexMatcher = regex.matcher(a_string);
        while (regexMatcher.find()) {
            matchList.add(regexMatcher.group());
        }
        return matchList.toArray(new String[matchList.size()]);
    }
}
