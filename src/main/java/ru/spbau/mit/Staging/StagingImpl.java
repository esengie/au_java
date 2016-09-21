package ru.spbau.mit.Staging;

import org.apache.commons.io.FileUtils;
import ru.spbau.mit.Revisions.CommitNode;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class StagingImpl implements Staging {
    private final Path m_root;
    private final String m_saveDirectoryName = ".asd";
    private final String m_stagingArea = m_saveDirectoryName + "/staging";

    public StagingImpl(){
        m_root = Paths.get(".");
        new File(m_root.toString() + m_saveDirectoryName).mkdir();
        new File(m_root.toString() + m_stagingArea).mkdir();
    }

    @Override
    public void add(Path a_file) throws IOException {
        String relativePath = relativize(m_root.toAbsolutePath(), a_file.toAbsolutePath());
        String stagingPath = m_root.toString() + m_stagingArea + relativePath;

        if (a_file.toFile().isFile()) {
            FileUtils.copyFile(a_file.toFile(), new File(stagingPath));
        }
        if (a_file.toFile().isDirectory()) {
            FileUtils.copyDirectory(a_file.toFile(), new File(stagingPath));
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
        String path = m_root.toString() + m_saveDirectoryName + number.toString();
        File commitDirectory = new File(path);
        FileUtils.copyDirectory(
                new File(m_root.toString() + m_stagingArea),
                commitDirectory);
        emptyStagingArea();
    }

    @Override
    public void emptyStagingArea() throws IOException {
        FileUtils.deleteDirectory(new File(m_root.toString() + m_stagingArea));
        new File(m_root.toString() + m_stagingArea).mkdir();
    }

}
