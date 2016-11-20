package ru.spbau.mit.Apps;

import org.apache.commons.cli.*;

class ClientLaunchArgs {
    static final String PORT_ARG_NAME = "port";
    static final String STATE_DIR_ARG_NAME = "stateDir";
    static final String TRACKER_ADDR_ARG_NAME = "tracker";
    private static final Options OPTIONS = new Options();

    static {
        OPTIONS.addOption(PORT_ARG_NAME, true, "local port to start seeding");
        OPTIONS.addOption(STATE_DIR_ARG_NAME, true, "directory for state");
        OPTIONS.addOption(TRACKER_ADDR_ARG_NAME, true, "tracker location");
    }

    static void launchUsage() {
        System.out.println("Usage for launching:\n -port mySeedPort\n -stateDir whereToSaveState\n -tracker hostName");
    }

    static CommandLine parseArgs(String[] args) throws ParseException {
        CommandLineParser parser = new DefaultParser();
        CommandLine cmdLine = parser.parse(OPTIONS, args);

        if (!cmdLine.hasOption(PORT_ARG_NAME)) {
            throw new ParseException("didn't specify port");
        }
        if (!cmdLine.hasOption(TRACKER_ADDR_ARG_NAME)) {
            throw new ParseException("didn't specify tracker address");
        }
        if (!cmdLine.hasOption(STATE_DIR_ARG_NAME)) {
            throw new ParseException("didn't specify directory to save/load state");
        }

        return cmdLine;
    }

}
