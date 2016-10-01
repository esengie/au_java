package ru.spbau.mit.Staging;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FileFileFilter;
import org.apache.commons.io.filefilter.NotFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import ru.spbau.mit.Paths.SaveDirLocation;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

class StagingPathHelpers {
    /**
     * Returns a relative path: second minus first
     *
     * @param a_root     shorter path
     * @param a_filePath longer path
     * @return difference betwen the two
     */
    static String relativize(String a_root, String a_filePath) {
        return new File(a_root)
                .toURI()
                .relativize(new File(a_filePath).toURI())
                .getPath();
    }

    /**
     * Lists all files' relative paths in a directory
     *
     * @param a_path . dir from which we start - must be absolute
     * @return all files listed (but not directories)
     */
    static List<String> listAllFilesRecursively(String a_path) {
        Collection<File> files = FileUtils.listFiles(new File(a_path), FileFileFilter.FILE, TrueFileFilter.INSTANCE);

        return files.stream()
                .map(File::getAbsolutePath)
                .map(s -> s.substring(a_path.length() + 1))
                .collect(Collectors.toList());
    }

    static void eraseWorkingDir(String a_path) throws IOException {
        for (File f : new File(a_path).getAbsoluteFile().listFiles((FileFilter)
                new NotFileFilter(
                        new WildcardFileFilter(SaveDirLocation.getFolderName()))
        )) {
            if (f.isDirectory())
                FileUtils.deleteDirectory(f);
            else
                FileUtils.deleteQuietly(f);
        }
    }
}
