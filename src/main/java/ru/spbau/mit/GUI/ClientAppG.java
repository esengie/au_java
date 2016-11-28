package ru.spbau.mit.GUI;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.ParseException;
import ru.spbau.mit.CLIApps.ClientLaunchArgs;
import ru.spbau.mit.TorrentClient.TorrentClient;
import ru.spbau.mit.TorrentClient.TorrentClientImpl;
import ru.spbau.mit.TorrentClient.TorrentFile.FileManager;

import java.io.File;
import java.io.IOException;

public class ClientAppG extends Application {
    private static FileManager fileManager;
    private static TorrentClient client;

    @FXML
    private Button uploadBtn;

    @FXML
    private VBox serverFiles;

    @FXML
    private VBox downloading;

//    @FXML
//    private TextField serverText;
//
//    @FXML
//    private HBox downloadingHBox;

    public static void main(String[] args2) {
        String args[] = {"-port", "8902", "-stateDir", ".", "-tracker", "localhost"};
        try {
            CommandLine cmd = ClientLaunchArgs.parseArgs(args);

            Short port = Short.parseShort(cmd.getOptionValue(ClientLaunchArgs.PORT_ARG_NAME));

            fileManager = new FileManager(new File(
                    cmd.getOptionValue(ClientLaunchArgs.STATE_DIR_ARG_NAME)));
            client = new TorrentClientImpl(fileManager, port);
//            client.connect(cmd.getOptionValue(ClientLaunchArgs.TRACKER_ADDR_ARG_NAME));
            launch(args);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            ClientLaunchArgs.launchUsage();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            SplitPane page = FXMLLoader.load(this.getClass().getResource("/ui.fxml"));
            Scene scene = new Scene(page);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Torrent Client");
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void initialize() {
        populateServerList();
    }

    private void populateServerList() {
        serverFiles.getChildren().add(createNoEditableTF("Momma"));
    }

    private static TextField createNoEditableTF(String text){
        TextField tf = new TextField();
        tf.setText(text);
        tf.setEditable(false);
        return tf;
    }

    @Override
    public void stop() throws Exception {
//        client.disconnect();
//        fileManager.saveToDisk();
        super.stop();
    }

}
