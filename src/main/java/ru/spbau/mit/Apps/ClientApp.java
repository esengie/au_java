package ru.spbau.mit.Apps;

import org.apache.commons.cli.*;
import ru.spbau.mit.Protocol.RemoteFile;
import ru.spbau.mit.TorrentClient.Client;
import ru.spbau.mit.TorrentClient.ClientImpl;
import ru.spbau.mit.TorrentClient.TorrentFile.FileManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class ClientApp {
    private static final String PORT_ARG_NAME = "port";
    private static final String STATE_DIR_ARG_NAME = "stateDir";
    private static final String TRACKER_ADDR_ARG_NAME = "tracker";
    private static final Options OPTIONS = new Options();
    static {
        OPTIONS.addOption(PORT_ARG_NAME, true, "local port to start seeding");
        OPTIONS.addOption(STATE_DIR_ARG_NAME, true, "directory for state");
        OPTIONS.addOption(TRACKER_ADDR_ARG_NAME, true, "tracker location");
    }

    public static void main(String[] args2) {
        // For debugging purposes only
        Logger log = LogManager.getLogManager().getLogger("");
        for (Handler h : log.getHandlers()) {
            h.setLevel(Level.INFO);
        }
        log.setLevel(Level.FINE);

        try {
            String[] args = {"-port", "8082", "-stateDir", ".", "-tracker", "localhost"};
            CommandLine cmd = parseArgs(args);

            Short port = Short.parseShort(cmd.getOptionValue(PORT_ARG_NAME));

            FileManager fileManager = new FileManager(new File(cmd.getOptionValue(STATE_DIR_ARG_NAME)));
            Client client = new ClientImpl(fileManager, port);

            client.connect(cmd.getOptionValue(TRACKER_ADDR_ARG_NAME));

            List<RemoteFile> lastList = new ArrayList<>();
            while (!client.isStopped()) {
                try {
                    String[] cmdArg = getUserInput().split(" ");
                    if (cmdArg.length == 0)
                        continue;
                    switch (cmdArg[0]) {
                        case "list": {
                            lastList = client.executeList();
                            printFiles(lastList);
                            break;
                        }
                        case "get": {
                            if (cmdArg.length < 2) {
                                System.out.println("get needs more args");
                                continue;
                            }
                            int id = Integer.parseInt(cmdArg[1]);
                            if (id >= lastList.size()){
                                System.out.println("you need to list first " +
                                        "and choose the id among listed files");
                                continue;
                            }
                            String dir = ".";
                            // by default download here
                            if (cmdArg.length > 2)
                                dir = cmdArg[2];

                            RemoteFile f = lastList.get(id);
                            client.executeGet(new File(dir), f);
                            System.out.println("File enqueued");
                            break;
                        }
                        case "sources": {
                            if (cmdArg.length < 2) {
                                System.out.println("sources needs more args");
                                continue;
                            }
                            int id = Integer.parseInt(cmdArg[1]);
                            List<InetSocketAddress> lst = client.executeSources(id);
                            lst.forEach(it -> System.out.println(it));
                            break;
                        }
                        case "upload": {
                            if (cmdArg.length < 2) {
                                System.out.println("upload needs more args");
                                continue;
                            }
                            File f = new File(cmdArg[1]);
                            if (!f.exists() || f.isDirectory())
                                throw new FileNotFoundException("There is no such file");
                            RemoteFile fileId = client.executeUpload(f);
                            lastList.add(fileId);
                            System.out.println(String.format("Uploaded with id = %d", fileId.id));
                            break;
                        }
                        case "q": {
                            client.disconnect();
                            fileManager.saveToDisk();
                            break;
                        }
                        default:
                            System.out.println("Unknown command, supported: q, upload filepath, get id, list ...");
                    }
                } catch (Exception e) {
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
        System.out.println(String.format("%4s|%20s|%8s", "ID", "NAME", "SIZE"));
        System.out.println("------------------------------------");
        for (RemoteFile file: files) {
            System.out.println(String.format("%4d|%20s|%8d",
                    file.id, file.name, file.size));
        }
        System.out.println("------------------------------------");
    }

    private static CommandLine parseArgs(String[] args) throws ParseException {
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
