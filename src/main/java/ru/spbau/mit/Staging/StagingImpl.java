package ru.spbau.mit.Staging;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.NotFileFilter;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import ru.spbau.mit.App.SaveDirLocation;
import ru.spbau.mit.Revisions.CommitNodes.CommitNode;
import ru.spbau.mit.Staging.Exceptions.FileShouldExistError;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.nio.file.Path;

public class StagingImpl implements Staging {
    private final Path m_root;
    private final String m_saveDirectoryName = SaveDirLocation.getFolderName();
    private final String m_stagingArea = m_saveDirectoryName + "/staging";

    public StagingImpl(Path a_root) {
        m_root = a_root;
        boolean res = new File(m_root + "/" + m_stagingArea).mkdirs();
    }

    @Override
    public void add(Path a_file) throws IOException {
        if (!a_file.toFile().exists())
            throw new FileShouldExistError(a_file.toString());

        String relativePath = relativize(m_root.toAbsolutePath(), a_file.toAbsolutePath());
        String stagingPath = m_root.toString() + "/" + m_stagingArea + "/" + relativePath;
        File movee = new File(stagingPath);
        FileUtils.forceMkdirParent(movee);

        if (a_file.toFile().isFile()) {
            FileUtils.copyFile(a_file.toFile(), movee);
        }
        if (a_file.toFile().isDirectory()) {
            FileUtils.copyDirectory(a_file.toFile(), movee);
        }
    }

    private String relativize(Path a_root, Path a_filePath) {
        return new File(a_root.toString())
                .toURI()
                .relativize(new File(a_filePath.toString()).toURI())
                .getPath();
    }

    @Override
    public void commitToDisk(CommitNode a_node) throws IOException {
        Integer number = a_node.getRevisionNumber();
        String path = m_root.toString() + "/" + m_saveDirectoryName + "/" + number.toString();
        File commitDirectory = new File(path);
        FileUtils.copyDirectory(
                new File(m_root.toString() + "/" + m_stagingArea),
                commitDirectory);
        emptyStagingArea();
    }

    @Override
    public void checkout(CommitNode a_node) throws IOException {
        Integer number = a_node.getRevisionNumber();
        String path = m_root.toString() + "/" + m_saveDirectoryName + "/" + number.toString();
        File commitDirectory = new File(path);

        for (File f : m_root.toFile().listFiles((FileFilter)
                new NotFileFilter(
                        new WildcardFileFilter(m_saveDirectoryName))
        )) {
            if (f.isDirectory())
                FileUtils.deleteDirectory(f);
            else
                FileUtils.deleteQuietly(f);
        }

        FileUtils.copyDirectory(
                commitDirectory,
                m_root.toFile());
        emptyStagingArea();
    }

    @Override
    public void emptyStagingArea() throws IOException {
        FileUtils.deleteDirectory(new File(m_root.toString() + "/" + m_stagingArea));
        new File(m_root.toString() + "/" + m_stagingArea).mkdir();
    }

}
