package ru.spbau.mit.App;

import com.beust.jcommander.ParameterException;
import ru.spbau.mit.App.Exceptions.RevisionTreeLoadException;
import ru.spbau.mit.AsdCommand.AsdCommand;
import ru.spbau.mit.AsdCommand.Exceptions.AlreadyAnAsdFolderException;
import ru.spbau.mit.AsdCommand.Exceptions.NotAnAsdFolderException;
import ru.spbau.mit.AsdCommand.Exceptions.SerializedStateNotFoundException;
import ru.spbau.mit.AsdCommand.InitCommand;
import ru.spbau.mit.Cli.Cli;
import ru.spbau.mit.Revisions.RevisionTree.RevisionTree;
import ru.spbau.mit.Revisions.RevisionTree.RevisionTreeImpl;
import ru.spbau.mit.Serialization.Serializer;
import ru.spbau.mit.Serialization.SerializerImpl;
import ru.spbau.mit.Staging.PersistentStaging;
import ru.spbau.mit.Staging.Staging;

import java.io.*;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static ru.spbau.mit.Paths.AsdFolderOperations.*;


/**
 * The main class, loads the Revision tree or does the init (used init command for that,
 * until realising that Java copies the references)
 * <p>
 * Loops until the input is gone and saves the tree
 * <p>
 * More robust option is to save the tree each time and launch the command each time,
 * but it's easier to test this way. Also AsdTest class is for manual testing.
 */
public class AsdVersionControlSystem {
    private RevisionTree m_tree;
    private Staging m_staging;
    private boolean loaded = false;

    /**
     * Loads the revision tree and the staging state
     * (depending on the staging implementation chosen may store all of the repo in memory)
     */
    private void loadState() throws SerializedStateNotFoundException, RevisionTreeLoadException {
        Serializer<RevisionTree> serializerTree = new SerializerImpl<>();
        Serializer<Staging> serializerStaging = new SerializerImpl<>();

        try {
            m_tree = serializerTree.deserialize(new FileInputStream(new File(getSerializedTreePath())));
            m_staging = serializerStaging.deserialize(new FileInputStream(new File(getSerializedStagingPath())));
        } catch (IOException e) {
            throw new SerializedStateNotFoundException("Something is wrong with .asd files, stopping");
        }

        if (m_tree == null || m_staging == null) {
            throw new RevisionTreeLoadException("Something is wrong with .asd files, couldn't load a Revision tree stopping");
        }
    }

    /**
     * You must save the state whenever you use asd class just before destroying it
     *
     * @throws NotAnAsdFolderException -- can't save if not inside an asd folder
     */
    public void saveState() throws NotAnAsdFolderException {
        if (!isAnAsdFolder())
            throw new NotAnAsdFolderException();

        Serializer<RevisionTree> serializerTree = new SerializerImpl<>();
        Serializer<Staging> serializerStaging = new SerializerImpl<>();

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

    /**
     * Init command is here to create the revision tree and staging
     *
     * @throws IOException io
     */
    private void initCommand() throws IOException {
        if (isAnAsdFolder()) {
            throw new AlreadyAnAsdFolderException();
        }
        m_staging = new PersistentStaging(Paths.get("").toFile().getAbsoluteFile().toPath());
        m_tree = new RevisionTreeImpl();
    }

    /**
     * If you want to use asd as a library use this
     *
     * @param line -- input line to git
     * @param out -- PrintStream to write to
     * @throws IOException -- cmd run throws and load state throws
     */
    public void runOnLine(String line, PrintStream out) throws IOException {
        if (line == null || line.trim().isEmpty()) return;

        AsdCommand cmd = Cli.parseAndDispatch(splitOnWhiteSpace(line));

        if (!isAnAsdFolder() && !InitCommand.class.isInstance(cmd))
            throw new NotAnAsdFolderException();

        if (!loaded && isAnAsdFolder()) {
            loadState();
            loaded = true;
        }

        if (InitCommand.class.isInstance(cmd)) {
            initCommand();
            loaded = true;
        } else
            cmd.run(m_tree, m_staging, out);
    }

    /**
     * Loops while we have input doing the git commands
     * <p>
     * Loads the repo if it was initted
     * Finishes and saves the repo state if it was initted
     *
     * @param argv - not used
     */
    public static void main(String... argv) {
        Scanner scanner = new Scanner(System.in);
        AsdVersionControlSystem asd = new AsdVersionControlSystem();
        while (scanner.hasNextLine()) {
            try {
                asd.runOnLine(scanner.nextLine(), System.out);
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
            System.out.println("Not an asd folder, couldn't create it or it was deleted");
        }
    }

    private static String[] splitOnWhiteSpace(String string) {
        List<String> matchList = new ArrayList<String>();
        Pattern regex = Pattern.compile("[^\\s\"']+|\"[^\"]*\"|'[^']*'");
        Matcher regexMatcher = regex.matcher(string);
        while (regexMatcher.find()) {
            matchList.add(regexMatcher.group());
        }
        return matchList.toArray(new String[matchList.size()]);
    }
}
