package ru.spbau.mit.Apps;

import javafx.application.Application;
import javafx.stage.Stage;
import org.apache.commons.cli.*;
import ru.spbau.mit.Protocol.RemoteFile;
import ru.spbau.mit.TorrentClient.Client;
import ru.spbau.mit.TorrentClient.ClientImpl;
import ru.spbau.mit.TorrentClient.TorrentFile.FileManager;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ClientAppG extends Application {
    private static FileManager fileManager;
    private static Client client;

    public static void main(String[] args2) {
        String args[] = {"-port", "8902", "-stateDir", ".", "-tracker", "localhost"};
        try {
            CommandLine cmd = ClientLaunchArgs.parseArgs(args);

            Short port = Short.parseShort(cmd.getOptionValue(ClientLaunchArgs.PORT_ARG_NAME));

            fileManager = new FileManager(new File(
                    cmd.getOptionValue(ClientLaunchArgs.STATE_DIR_ARG_NAME)));
            client = new ClientImpl(fileManager, port);
            client.connect(cmd.getOptionValue(ClientLaunchArgs.TRACKER_ADDR_ARG_NAME));
            launch(args);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            ClientLaunchArgs.launchUsage();
        } catch (IOException e){
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void start(Stage primaryStage) {

    }

    @Override
    public void stop() throws Exception {
        client.disconnect();
        fileManager.saveToDisk();
        super.stop();
    }

}
