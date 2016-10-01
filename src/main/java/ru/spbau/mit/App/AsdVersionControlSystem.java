package ru.spbau.mit.App;

import com.beust.jcommander.ParameterException;
import ru.spbau.mit.App.Exceptions.RevisionTreeLoadRuntimeException;
import ru.spbau.mit.AsdCommand.Exceptions.NotAnAsdFolderException;
import ru.spbau.mit.AsdCommand.Exceptions.SerializedStateNotFoundRuntimeException;
import ru.spbau.mit.AsdCommand.AsdCommand;
import ru.spbau.mit.AsdCommand.Exceptions.AlreadyAnAsdFolderException;
import ru.spbau.mit.AsdCommand.InitCommand;
import ru.spbau.mit.Cli.Cli;
import ru.spbau.mit.Revisions.RevisionTree.RevisionTree;
import ru.spbau.mit.Revisions.RevisionTree.RevisionTreeImpl;
import ru.spbau.mit.Revisions.RevisionTree.RevisionTreeSerializer;
import ru.spbau.mit.Revisions.RevisionTree.RevisionTreeSerializerImpl;
import ru.spbau.mit.Staging.*;

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


/**
 * The main class, loads the Revision tree or does the init (used init command for that,
 * until realising that Java copies the references)
 *
 * Loops until the input is gone and saves the tree
 *
 * More robust option is to save the tree each time and launch the command each time,
 * but it's easier to test this way. Also AsdTest class is for manual testing.
 */
public class AsdVersionControlSystem {
    private RevisionTree m_tree;
    private Staging m_staging;

    private void loadState() throws NotAnAsdFolderException {
        if (!isAnAsdFolder())
            throw new NotAnAsdFolderException();

        RevisionTreeSerializer serializerTree = new RevisionTreeSerializerImpl();
        StagingSerializer serializerStaging = new PersistentStagingSerializer();

        try {
            m_tree = serializerTree.deserialize(new FileInputStream(new File(getSerializedTreePath())));
            m_staging = serializerStaging.deserialize(new FileInputStream(new File(getSerializedStagingPath())));
        } catch (IOException e) {
            throw new SerializedStateNotFoundRuntimeException();
        }

        if (m_tree == null || m_staging == null)
            throw new RevisionTreeLoadRuntimeException();
    }

    private void saveState() throws NotAnAsdFolderException {
        if (!isAnAsdFolder())
            throw new NotAnAsdFolderException();

        RevisionTreeSerializer serializerTree = new RevisionTreeSerializerImpl();
        StagingSerializer serializerStaging = new PersistentStagingSerializer();

        File outTree = new File(getSerializedTreePath());
        File outStaging = new File(getSerializedStagingPath());
        try {
            outTree.createNewFile();
            outStaging.createNewFile();
            serializerTree.serialize(m_tree, new FileOutputStream(outTree));
            serializerStaging.serialize(m_staging, new FileOutputStream(outStaging));

        } catch (IOException e) {
            throw new IllegalStateException("Unknown error during saving the tree", e);
        }

    }

    private void initCommand() throws IOException {
        if (isAnAsdFolder()) {
            throw new AlreadyAnAsdFolderException();
        }
        m_staging = new PersistentStaging(Paths.get("").toFile().getAbsoluteFile().toPath());
        m_tree = new RevisionTreeImpl();
    }

    public static void main(String... argv) {
        Scanner scanner = new Scanner(System.in);
        AsdVersionControlSystem asd = new AsdVersionControlSystem();
        boolean loaded = false;
        while (scanner.hasNextLine()) {
            try {
                String line = scanner.nextLine();
                if (line == null || line.trim().isEmpty()) continue;

                AsdCommand cmd = Cli.parseAndDispatch(splitOnWhiteSpace(line));

                if (!isAnAsdFolder() && !InitCommand.class.isInstance(cmd))
                    throw new NotAnAsdFolderException();

                if (!loaded && isAnAsdFolder()) {
                    asd.loadState();
                    loaded = true;
                }

                if (InitCommand.class.isInstance(cmd)) {
                    asd.initCommand();
                    loaded = true;
                } else
                    cmd.run(asd.m_tree, asd.m_staging, System.out);

            } catch (AlreadyAnAsdFolderException e) {
                System.out.println("Can't init an asd folder");
            } catch (NotAnAsdFolderException e) {
                System.out.println("Not an asd folder you should init first");
            } catch (ParameterException e) {
                System.out.println(e.getMessage());
                Cli.getParser().usage();
            } catch (IOException e) {
                System.out.println(e);
            }
        }

        try {
            asd.saveState();
        } catch (NotAnAsdFolderException notAnAsdFolder) {
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
