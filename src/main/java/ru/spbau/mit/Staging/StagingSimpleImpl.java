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
 * doesn't support the new commands, namely status, remove and reset
 * 
 */
public class StagingSimpleImpl implements Staging, Serializable {
    private final String m_root;
    private static final String m_saveDirectoryName = SaveDirLocation.getFolderName();
    private static final String m_stagingArea = m_saveDirectoryName + "/staging";

    public StagingSimpleImpl(Path root) throws IOException {
        m_root = root.toAbsolutePath().toString();
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
    public void add(Path file) throws IOException {
        if (!file.toFile().getAbsoluteFile().exists())
            throw new FileShouldExistException(file.toString());

        String relativePath = relativize(m_root,
                file.toAbsolutePath().toString());
        String stagingPath = m_root + "/" + m_stagingArea + "/" + relativePath;
        File movee = new File(stagingPath);
        FileUtils.forceMkdirParent(movee);

        if (file.toFile().getAbsoluteFile().isFile()) {
            FileUtils.copyFile(file.toFile().getAbsoluteFile(), movee);
        }
        if (file.toFile().getAbsoluteFile().isDirectory()) {
            FileUtils.copyDirectoryToDirectory(file.toFile().getAbsoluteFile(), movee);
        }
    }

    @Override
    public void commitToDisk(CommitNode node) throws IOException {
        Integer number = node.getRevisionNumber();
        String path = m_root.toString() + "/" + m_saveDirectoryName + "/" + number.toString();
        File commitDirectory = new File(path);
        FileUtils.copyDirectory(
                new File(m_root.toString() + "/" + m_stagingArea),
                commitDirectory);
    }

    @Override
    public void checkout(CommitNode node) throws IOException {
        Integer number = node.getRevisionNumber();
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
    public void merge(CommitNode from, CommitNode to, CommitNode result) throws IOException {
        Set<String> fromFiles = new TreeSet<>(listAllFilesRecursively(commitNodeToPath(from)));
        Set<String> toFiles = new TreeSet<>(listAllFilesRecursively(commitNodeToPath(to)));

        Set<String> set = new TreeSet<>(fromFiles);
        set.retainAll(toFiles);

        for (String p : set) {
            if (filesDiffer(p, from, to))
                throw new CantMergeException(p);
        }

        copyFilesFromOneCommitToAnother(fromFiles.stream().collect(Collectors.toList()), from, result);
        toFiles.removeAll(fromFiles);
        copyFilesFromOneCommitToAnother(toFiles.stream().collect(Collectors.toList()), to, result);

        checkout(result);
    }

    @Override
    public void reset(Path file) throws IOException {
        throw new NotImplementedException();
    }

    @Override
    public void remove(Path file) throws IOException {
        throw new NotImplementedException();
    }

    @Override
    public RepoStatus status() throws IOException {
        throw new NotImplementedException();
    }

    @Override
    public void emptyStagingArea() throws IOException {
        FileUtils.deleteDirectory(new File(m_root + "/" + m_stagingArea));
        new File(m_root + "/" + m_stagingArea).mkdir();
    }

    private void copyFilesFromOneCommitToAnother(List<String> paths, CommitNode from, CommitNode to) throws IOException {
        String prefixFrom = commitNodeToPath(from);
        File folderTo = new File(commitNodeToPath(to));
        for (String s : paths) {
            FileUtils.copyFileToDirectory(new File(prefixFrom + "/" + s), folderTo);
        }
    }

    private boolean filesDiffer(String path, CommitNode left, CommitNode right) throws IOException {
        String leftPath = commitNodeToPath(left) + "/" + path;
        String rightPath = commitNodeToPath(right) + "/" + path;

        return !FileUtils.contentEquals(new File(leftPath), new File(rightPath));
    }

    private String commitNodeToPath(CommitNode node) {
        return m_root + "/" + m_saveDirectoryName + "/" + String.valueOf(node.getRevisionNumber());
    }

}


