package ru.spbau.mit.Apps;

import ru.spbau.mit.Server.Server;
import ru.spbau.mit.Server.ServerImpl;

import java.io.IOException;
import java.util.Scanner;
import java.util.logging.Logger;

public class ServerApp {
    private static final Logger logger = Logger.getLogger(ServerApp.class.getName());

    public static void main(String[] args2) {
        try {
            String[] args = {"8002"};
            Server s = new ServerImpl();
            if (args.length < 1) {
                System.out.println("Need a port to run on");
                return;
            }
            short port = Short.parseShort(args[0]);
            s.start(port);
            runServer(s);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void runServer(Server s) throws IOException {
        while (!getUserInput().equals("q")) {
        }
        s.stop();
    }

    private static String getUserInput() {
        return new Scanner(System.in).nextLine();
    }
}