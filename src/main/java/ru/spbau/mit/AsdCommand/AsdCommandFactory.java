package ru.spbau.mit.AsdCommand;

import ru.spbau.mit.Exceptions.CommandCreationException;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AsdCommandFactory {
    private static final Map<String, Class<? extends AsdCommand>> COMMANDS = new ConcurrentHashMap<>();

    static {
        COMMANDS.put("add", Add.class);
        COMMANDS.put("branch", Branch.class);
        COMMANDS.put("checkout", Checkout.class);
        COMMANDS.put("commit", Commit.class);
        COMMANDS.put("log", Log.class);
        COMMANDS.put("merge", Merge.class);
    }

    public static AsdCommand createCommand(String a_commandName, String... a_commandArguments) throws CommandCreationException {
        List<String> args = Arrays.asList(a_commandArguments);

        Class<? extends AsdCommand> commandClass = COMMANDS.get(a_commandName);
        try {
            Constructor<? extends AsdCommand> constructor = commandClass.getDeclaredConstructor(List.class);
            return constructor.newInstance(args);
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            throw new CommandCreationException(a_commandName, e);
        }
    }
}
