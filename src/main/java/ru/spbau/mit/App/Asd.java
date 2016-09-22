package ru.spbau.mit.App;

import com.sun.org.apache.xpath.internal.SourceTree;
import ru.spbau.mit.AsdCommand.AsdCommand;
import ru.spbau.mit.AsdCommand.Exceptions.AlreadyAnAsdFolderException;
import ru.spbau.mit.Cli.Cli;
import ru.spbau.mit.Revisions.RevisionTree.RevisionTree;
import ru.spbau.mit.Staging.Staging;

import java.io.IOException;
import java.util.Scanner;

public class Asd {
    private RevisionTree m_tree;
    private Staging m_staging;
    private void loadRevisionTree(){

    }
    public static int main(String... argv) {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            try {
                String line = scanner.nextLine();
                if (line == null) continue;
                AsdCommand cmd = Cli.parseAndDispatch(line.split("\\s"));
                cmd.run();
            }
            catch (AlreadyAnAsdFolderException e) {
                System.out.println(e.getMessage());
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }

        return 0;
    }
}
