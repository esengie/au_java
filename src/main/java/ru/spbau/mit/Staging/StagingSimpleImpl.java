package ru.spbau.mit.Staging;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.NotFileFilter;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.apache.commons.lang.NotImplementedException;
import ru.spbau.mit.Paths.SaveDirLocation;
import ru.spbau.mit.Revisions.CommitNodes.CommitNode;
import ru.spbau.mit.Staging.Exceptions.CantMergeException;
import ru.spbau.mit.Staging.Exceptions.FileShouldExistException;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import static ru.spbau.mit.Staging.StagingPathHelpers.*;

/**
 * First version of staging implementation - copies the repo,
 * doesn't support the new commands
 */
public class StagingSimpleImpl implements Staging, Serializable {
    private final String m_root;
    private static final String m_saveDirectoryName = SaveDirLocation.getFolderName();
    private static final String m_stagingArea = m_saveDirectoryName + "/staging";

    public StagingSimpleImpl(Path a_root) throws IOException {
        m_root = a_root.toAbsolutePath().toString();
        boolean res = new File(m_root + "/" + m_stagingArea).mkdirs();
        if (!res)
            return;
        String relativePath = m_root + "/" + m_saveDirectoryName + "/0";
        File dest = new File(relativePath);
        for (File f : new File(m_root)
                .getAbsoluteFile()
                .listFiles((FileFilter) new NotFileFilter(
                        new WildcardFileFilter(m_saveDirectoryName))
                )) {
            if (f.getAbsoluteFile().isDirectory()) {
                String from = relativize(m_root, f.getAbsolutePath());
                FileUtils.copyDirectoryToDirectory(f.getAbsoluteFile(), new File(relativePath + "/" + from));
            } else
                FileUtils.copyFileToDirectory(f, dest);
        }
    }

    @Override
    public void add(Path a_file) throws IOException {
        if (!a_file.toFile().getAbsoluteFile().exists())
            throw new FileShouldExistException(a_file.toString());

        String relativePath = relativize(m_root,
                a_file.toAbsolutePath().toString());
        String stagingPath = m_root + "/" + m_stagingArea + "/" + relativePath;
        File movee = new File(stagingPath);
        FileUtils.forceMkdirParent(movee);

        if (a_file.toFile().getAbsoluteFile().isFile()) {
            FileUtils.copyFile(a_file.toFile().getAbsoluteFile(), movee);
        }
        if (a_file.toFile().getAbsoluteFile().isDirectory()) {
            FileUtils.copyDirectoryToDirectory(a_file.toFile().getAbsoluteFile(), movee);
        }
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
        String path = m_root + "/" + m_saveDirectoryName + "/" + number.toString();
        File commitDirectory = new File(path);

        eraseWorkingDir(m_root);

        FileUtils.copyDirectory(
                commitDirectory,
                new File(m_root).getAbsoluteFile());

        emptyStagingArea();
        FileUtils.copyDirectory(
                commitDirectory,
                new File(m_root + "/" + m_stagingArea));

    }

    @Override
    public void merge(CommitNode a_from, CommitNode a_to, CommitNode a_result) throws IOException {
        Set<String> from = new TreeSet<>(listAllFilesRecursively(commitNodeToPath(a_from)));
        Set<String> to = new TreeSet<>(listAllFilesRecursively(commitNodeToPath(a_to)));

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
    public void reset(Path a_file) throws IOException {
        throw new NotImplementedException();
    }

    @Override
    public void remove(Path a_file) throws IOException {
        throw new NotImplementedException();
    }

    @Override
    public void emptyStagingArea() throws IOException {
        FileUtils.deleteDirectory(new File(m_root + "/" + m_stagingArea));
        new File(m_root + "/" + m_stagingArea).mkdir();
    }

    private void copyFilesFromOneCommitToAnother(List<String> a_paths, CommitNode a_from, CommitNode a_to) throws IOException {
        String prefixFrom = commitNodeToPath(a_from);
        File folderTo = new File(commitNodeToPath(a_to));
        for (String s : a_paths) {
            FileUtils.copyFileToDirectory(new File(prefixFrom + "/" + s), folderTo);
        }
    }

    private boolean filesDiffer(String a_path, CommitNode a_left, CommitNode a_right) throws IOException {
        String left = commitNodeToPath(a_left) + "/" + a_path;
        String right = commitNodeToPath(a_right) + "/" + a_path;

        return !FileUtils.contentEquals(new File(left), new File(right));
    }

    private String commitNodeToPath(CommitNode a_node) {
        return m_root + "/" + m_saveDirectoryName + "/" + String.valueOf(a_node.getRevisionNumber());
    }

}


