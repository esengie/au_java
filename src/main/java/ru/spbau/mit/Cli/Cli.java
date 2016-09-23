package ru.spbau.mit.Cli;

import com.beust.jcommander.JCommander;
import ru.spbau.mit.AsdCommand.AsdCommand;
import ru.spbau.mit.AsdCommand.AsdCommandFactory;
import ru.spbau.mit.AsdCommand.Exceptions.AlreadyAnAsdFolderException;
import ru.spbau.mit.AsdCommand.Exceptions.CommandCreationError;


/**
 * The Cli.
 *
 * Because Jcommander is intended for single use at the start of the program
 * and not in the loop as used here I have a Setup method and I also return the instance
 * for usage printing -- JCommander's fault
 */
public class Cli {
    private static JCommander jCommander;

    private Cli() {
    }

    private static void Setup() throws CommandCreationError {
        Cli cli = new Cli();
        jCommander = new JCommander(cli);
        AsdCommandFactory.getCommandNames().forEach(s ->
                jCommander.addCommand(s, AsdCommandFactory.createCommand(s)));
    }

    public static AsdCommand parseAndDispatch(String... line) throws CommandCreationError {
        Setup();
        jCommander.parse(line);

        String parsedCommand = jCommander.getParsedCommand();
        JCommander parsedJCommander = jCommander.getCommands().get(parsedCommand);
        return (AsdCommand) parsedJCommander.getObjects().get(0);
    }

    // Kostyl tk biblioteka zdes, for printing usage
    public static JCommander getParser(){
        Setup();
        return jCommander;
    }

}