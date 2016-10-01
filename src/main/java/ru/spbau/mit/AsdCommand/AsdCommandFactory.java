package ru.spbau.mit.AsdCommand;

import ru.spbau.mit.AsdCommand.Exceptions.CommandCreationRuntimeException;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AsdCommandFactory {
    private static final Map<String, Class<? extends AsdCommand>> COMMANDS = new ConcurrentHashMap<>();

    static {
        COMMANDS.put("add", AddCommand.class);
        COMMANDS.put("branch", BranchCommand.class);
        COMMANDS.put("checkout", CheckoutCommand.class);
        COMMANDS.put("commit", CommitCommand.class);
        COMMANDS.put("log", LogCommand.class);
        COMMANDS.put("merge", MergeCommand.class);
        COMMANDS.put("init", InitCommand.class);
    }

    public static List<String> getCommandNames(){
        return new ArrayList<>(COMMANDS.keySet());
    }

    public static AsdCommand createCommand(String a_commandName) {
        Class<? extends AsdCommand> commandClass = COMMANDS.get(a_commandName);
        try {
            Constructor<? extends AsdCommand> constructor = commandClass.getDeclaredConstructor();
            return constructor.newInstance();
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            throw new CommandCreationRuntimeException(a_commandName, e);
        }
    }
}
