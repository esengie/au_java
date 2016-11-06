package ru.spbau.mit.Apps;

import ru.spbau.mit.TorrentServer.Server;
import ru.spbau.mit.TorrentServer.ServerImpl;

import java.io.File;
import java.io.IOException;

public class ServerApp {
    public static void main(String[] args) {
        System.out.println("Starting the server on port 8081");
        Server s = new ServerImpl();
        try {
            if (args.length < 1)
                s.start(new File(".").getAbsoluteFile());
            if (args.length > 1)
                throw new IOException("Too many args specified");
            File config = new File(args[0]);
            s.start(config);
        } catch (IOException e) {
            System.out.println(e.toString());
        }
    }
}
