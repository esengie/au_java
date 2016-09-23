package ru.spbau.mit.Staging;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.*;
import ru.spbau.mit.Paths.SaveDirLocation;
import ru.spbau.mit.Revisions.CommitNodes.CommitNode;
import ru.spbau.mit.Staging.Exceptions.CantMergeException;
import ru.spbau.mit.Staging.Exceptions.FileShouldExistException;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

public class StagingImpl implements Staging {
    private final Path m_root;
    private final String m_saveDirectoryName = SaveDirLocation.getFolderName();
    private final String m_stagingArea = m_saveDirectoryName + "/staging";

    public StagingImpl(Path a_root) throws IOException {
        m_root = a_root;
        boolean res = new File(m_root + "/" + m_stagingArea).mkdirs();

        File dest = new File(m_root + "/" + m_saveDirectoryName + "/0");
        for (File f : m_root.toFile().getAbsoluteFile().listFiles((FileFilter)
                new NotFileFilter(
                        new WildcardFileFilter(m_saveDirectoryName))
        )) {
            if (f.isDirectory())
                FileUtils.copyDirectoryToDirectory(f, dest);
            else
                FileUtils.copyFileToDirectory(f, dest);
        }
    }

    @Override
    public void add(Path a_file) throws IOException {
        if (!a_file.toFile().getAbsoluteFile().exists())
            throw new FileShouldExistException(a_file.toString());

        String relativePath = relativize(m_root.toAbsolutePath(),
                a_file.toAbsolutePath());
        String stagingPath = m_root.toAbsolutePath() + "/" + m_stagingArea + "/" + relativePath;
        File movee = new File(stagingPath);
        FileUtils.forceMkdirParent(movee);

        if (a_file.toFile().getAbsoluteFile().isFile()) {
            FileUtils.copyFile(a_file.toFile().getAbsoluteFile(), movee);
        }
        if (a_file.toFile().getAbsoluteFile().isDirectory()) {
            FileUtils.copyDirectoryToDirectory(a_file.toFile().getAbsoluteFile(), movee);
        }
    }

    private String relativize(Path a_root, Path a_filePath) {
        return new File(a_root.toAbsolutePath().toString())
                .toURI()
                .relativize(new File(a_filePath.toAbsolutePath().toString()).toURI())
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
    }

    @Override
    public void checkout(CommitNode a_node) throws IOException {
        Integer number = a_node.getRevisionNumber();
        String path = m_root.toString() + "/" + m_saveDirectoryName + "/" + number.toString();
        File commitDirectory = new File(path);

        for (File f : m_root.toFile().getAbsoluteFile().listFiles((FileFilter)
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
                m_root.toFile().getAbsoluteFile());

        emptyStagingArea();
        FileUtils.copyDirectory(
                commitDirectory,
                new File(m_root.toAbsolutePath() + "/" + m_stagingArea));

    }

    @Override
    public void merge(CommitNode a_from, CommitNode a_to, CommitNode a_result) throws IOException {
        Set<String> from = new TreeSet<>(listAllFilesRecursively(a_from));
        Set<String> to = new TreeSet<>(listAllFilesRecursively(a_to));

        Set<String> set = new TreeSet<>(from);
        set.retainAll(to);

        for (String p : set) {
            if (filesDiffer(p, a_from, a_to))
                throw new CantMergeException(p);
        }

        copyFilesFromOneCommitToAnother(from.stream().collect(Collectors.toList()), a_from, a_result);
        to.removeAll(from);
        copyFilesFromOneCommitToAnother(to.stream().collect(Collectors.toList()), a_to, a_result);

        checkout(a_result);
    }

    @Override
    public void emptyStagingArea() throws IOException {
        FileUtils.deleteDirectory(new File(m_root.toAbsolutePath() + "/" + m_stagingArea));
        new File(m_root.toAbsolutePath() + "/" + m_stagingArea).mkdir();
    }

    private void copyFilesFromOneCommitToAnother(List<String> a_paths, CommitNode a_from, CommitNode a_to) throws IOException {
        String prefixFrom = commitNodeToPath(a_from);
        File folderTo = new File(commitNodeToPath(a_to));
        for (String s : a_paths) {
            FileUtils.copyFileToDirectory(new File(prefixFrom + s), folderTo);
        }
    }

    private boolean filesDiffer(String a_path, CommitNode a_left, CommitNode a_right) throws IOException {
        String left = commitNodeToPath(a_left) + a_path;
        String right = commitNodeToPath(a_right) + a_path;

        return FileUtils.contentEquals(new File(left), new File(right));
    }

    private String commitNodeToPath(CommitNode a_node) {
        return m_root.toAbsolutePath() + "/" + String.valueOf(a_node.getRevisionNumber());
    }

    private List<String> listAllFilesRecursively(CommitNode a_node) {
        String path = commitNodeToPath(a_node);
        Collection<File> files = FileUtils.listFiles(new File(path), FileFileFilter.FILE, TrueFileFilter.INSTANCE);

        return files.stream()
                .map(File::getAbsolutePath)
                .collect(Collectors.toList());
    }
}
