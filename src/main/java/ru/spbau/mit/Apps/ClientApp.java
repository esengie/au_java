package ru.spbau.mit.Apps;

import org.apache.commons.cli.*;
import ru.spbau.mit.Client.Client;
import ru.spbau.mit.Client.ClientImpl;
import ru.spbau.mit.Protocol.RemoteFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class ClientApp {
    private static final String PORT_ARG_NAME = "port";
    private static final String SERVER_ADDR_ARG_NAME = "server";
    private static final Options OPTIONS = new Options();
    static {
        OPTIONS.addOption(PORT_ARG_NAME, true, "local port to start seeding");
        OPTIONS.addOption(SERVER_ADDR_ARG_NAME, true, "server location");
    }

    public static void main(String[] args2) {
        try {
            String[] args = {"-port", "8002", "-server", "localhost"};
            CommandLine cmd = parseArgs(args);

            Short port = Short.parseShort(cmd.getOptionValue(PORT_ARG_NAME));

            Client client = new ClientImpl();

            client.connect(cmd.getOptionValue(SERVER_ADDR_ARG_NAME), port);

            boolean runnin = true;
            while (runnin) {
                try {
                    String[] cmdArg = getUserInput().split(" ");
                    if (cmdArg.length == 0)
                        continue;
                    switch (cmdArg[0]) {
                        case "list": {
                            if (cmdArg.length < 2) {
                                System.out.println("list needs more args");
                                continue;
                            }
                            List<RemoteFile> lastList = client.executeList(cmdArg[1]);
                            printFiles(lastList);
                            break;
                        }
                        case "get": {
                            File dir = new File(".");
                            if (cmdArg.length > 2) {
                                dir = new File(cmdArg[2]);
                            }
                            File f = new File(cmdArg[1]);
                            File to = new File(dir, f.getName());
                            if (to.createNewFile()) {
                                client.executeGet(cmdArg[1], new FileOutputStream(to));
                            } else {
                                System.out.println("File " + cmdArg[1] + " already exists in " + cmdArg[2]);
                            }
                            break;
                        }
                        case "q": {
                            client.disconnect();
                            runnin = false;
                            break;
                        }
                        default:
                            System.out.println("Unknown command, supported: q, get path dirToSave, list path");
                    }
                } catch (IOException e) {
                    System.out.println(String.format("error: %s", e.getMessage()));
                }
            }
        } catch (ParseException | IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private static String getUserInput() {
        return new Scanner(System.in).nextLine();
    }

    private static void printFiles(List<RemoteFile> files) {
        System.out.println(String.format("%1s|%20s", " ", "Name"));
        System.out.println("------------------------------------");
        for (RemoteFile file: files) {
            System.out.println(String.format("%1s|%20s",
                    file.isDir ? "d" : " ", file.path));
        }
        System.out.println("------------------------------------");
    }

    private static CommandLine parseArgs(String[] args) throws ParseException {
        CommandLineParser parser = new DefaultParser();
        CommandLine cmdLine = parser.parse(OPTIONS, args);

        if (!cmdLine.hasOption(PORT_ARG_NAME)) {
            throw new ParseException("didn't specify port");
        }
        if (!cmdLine.hasOption(SERVER_ADDR_ARG_NAME)) {
            throw new ParseException("didn't specify server address");
        }

        return cmdLine;
    }
}