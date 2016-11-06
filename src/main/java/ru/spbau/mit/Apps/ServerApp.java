package ru.spbau.mit.Apps;

import ru.spbau.mit.TorrentServer.Server;
import ru.spbau.mit.TorrentServer.ServerImpl;
import ru.spbau.mit.TorrentServer.TorrentIOException;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class ServerApp {
    private static final Logger logger = Logger.getLogger(ServerApp.class.getName());

    public static void main(String[] args) {
        logger.log(Level.FINE, "Starting the server on port 8081");
        Server s = new ServerImpl();
        try {
            if (args.length < 1) {
                s.start(new File(".").getAbsoluteFile());
                runServer(s);
                return;
            }
            if (args.length > 1)
                throw new IOException("Too many args specified");
            File config = new File(args[0]);
            s.start(config);
            runServer(s);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void runServer(Server s) throws TorrentIOException {
        while (!getUserInput().equals("q")){ }
        s.stop();
    }

    private static String getUserInput() {
        return new Scanner(System.in).nextLine();
    }
}
