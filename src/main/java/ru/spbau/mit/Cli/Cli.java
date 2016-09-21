package ru.spbau.mit.Cli;

import com.beust.jcommander.JCommander;
import ru.spbau.mit.AsdCommand.AsdCommand;
import ru.spbau.mit.AsdCommand.AsdCommandFactory;
import ru.spbau.mit.AsdCommand.Exceptions.CommandCreationError;

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

    // AddCommand error handling for parsing
    public static AsdCommand parseAndDispatch(String... line) throws CommandCreationError {
        // Because it's intended for single use at the start of the program
        Setup();
        jCommander.parse(line);

        String parsedCommand = jCommander.getParsedCommand();
        JCommander parsedJCommander = jCommander.getCommands().get(parsedCommand);
        return (AsdCommand) parsedJCommander.getObjects().get(0);
    }

    public static void main(String[] args) throws Exception {
        AsdCommand cmd = parseAndDispatch("add one two three".split("\\s"));
        cmd.run();
        cmd = parseAndDispatch("add four five six".split("\\s"));
        cmd.run();

//        Scanner scanner = new Scanner(System.in);
//        while (scanner.hasNextLine()) {
//            String line = scanner.nextLine();
//            if (line == null) continue;
//            AsdCommand cmd = parseAndDispatch(line.split("\\s"));
//            cmd.run();
//        }
    }

}