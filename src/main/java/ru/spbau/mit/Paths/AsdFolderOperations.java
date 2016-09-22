package ru.spbau.mit.Paths;

import com.sun.istack.internal.NotNull;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public class AsdFolderOperations {
    private AsdFolderOperations(){}


    @NotNull
    public static String getSerializedTreePath() {
        return getRoot().toAbsolutePath() + "/" +
                SaveDirLocation.getFolderName() + "/" +
                RevisionTreeFileName.getFileName();
    }

    public static boolean isAnAsdFolder() {
        return getRoot() != null;
    }

    public static Path getRoot() {
        Path folder = Paths.get("").toAbsolutePath();
        while (true) {
            if (!(new File(folder.toString(),
                    SaveDirLocation.getFolderName()).exists()))
                return folder;
            folder = folder.getParent();
            if (folder == null)
                return null;
        }
    }
}
