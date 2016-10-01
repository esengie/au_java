package ru.spbau.mit.Staging;

import org.apache.commons.io.FileUtils;
import org.organicdesign.fp.collections.PersistentTreeMap;
import ru.spbau.mit.Paths.SaveDirLocation;
import ru.spbau.mit.Revisions.CommitNodes.CommitNode;
import ru.spbau.mit.Staging.Exceptions.CantMergeException;
import ru.spbau.mit.Staging.Exceptions.FileShouldExistException;
import ru.spbau.mit.Staging.Exceptions.CommitNumberConflictRuntimeException;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

import static ru.spbau.mit.Staging.StagingPathHelpers.*;

import static ru.spbau.mit.Staging.StagingPathHelpers.relativize;

/**
 * Does the staging stage persistently
 */
public class PersistentStaging implements Staging, Serializable {
    private final String m_root;
    private final List<PersistentTreeMap<String, byte[]>> m_commits = new ArrayList<>();
    private int m_currentCommit = 0;
    private PersistentTreeMap<String, byte[]> m_staging;


    /**
     * This constructor should be called only when there's no state saved on disk
     * Deserialize otherwise. Important because staging area is attached to the last commit
     *
     * @param a_root path to working folder
     */
    public PersistentStaging(Path a_root) throws IOException {
        m_root = a_root.toAbsolutePath().toString();
        m_staging = PersistentTreeMap.empty();

        new File(m_root + "/" + SaveDirLocation.getFolderName()).mkdirs();
        add(Paths.get("").toAbsolutePath());
        m_commits.add(m_staging);
        m_currentCommit = m_commits.size() - 1;
    }

    @Override
    public void add(Path a_filePath) throws IOException {
        File file = a_filePath.toFile().getAbsoluteFile();
        if (!file.exists())
            throw new FileShouldExistException(a_filePath.toString());

        String relativePath = relativize(m_root,
                a_filePath.toAbsolutePath().toString());

        if (file.isFile()) {
            m_staging = m_staging.assoc(relativePath, FileUtils.readFileToByteArray(file));
        }
        if (file.isDirectory()) {
            if (!relativePath.isEmpty())
                relativePath = relativePath + "/";
            String relPath = relativePath;

            List<String> filePaths = listAllFilesRecursively(a_filePath.toAbsolutePath().toString());
            filePaths = filePaths.stream()
                    .map(s -> relPath + s)
                    .collect(Collectors.toList());
            for (String s : filePaths){
                m_staging = m_staging.assoc(s,
                        FileUtils.readFileToByteArray(new File(s).getAbsoluteFile()));
            }
        }
    }

    @Override
    public void commitToDisk(CommitNode a_node) {
        if (a_node.getRevisionNumber() != m_commits.size())
            throw new CommitNumberConflictRuntimeException();

        m_commits.add(m_staging);
        m_currentCommit = m_commits.size() - 1;
    }

    @Override
    public void emptyStagingArea() {
        m_staging = m_commits.get(m_currentCommit);
    }

    @Override
    public void checkout(CommitNode a_node) throws IOException {
        if (! (a_node.getRevisionNumber() < m_commits.size()))
            throw new CommitNumberConflictRuntimeException();

        eraseWorkingDir(m_root);

        PersistentTreeMap<String, byte[]> commitContents =
                m_commits.get(a_node.getRevisionNumber());

        for (Map.Entry<String, byte[]> it : commitContents.entrySet()){
            byteArrayToFile(m_root + "/" + it.getKey(), it.getValue());
        }

        m_currentCommit = a_node.getRevisionNumber();
        emptyStagingArea();
    }

    @Override
    public void merge(CommitNode a_from, CommitNode a_to, CommitNode a_result) throws IOException {
        PersistentTreeMap<String, byte[]> from = m_commits.get(a_from.getRevisionNumber());
        PersistentTreeMap<String, byte[]> to = m_commits.get(a_to.getRevisionNumber());

        if (a_result.getRevisionNumber() != m_commits.size())
            throw new CommitNumberConflictRuntimeException();

        Set<String> set = new TreeSet<>(from.keySet());
        set.retainAll(to.keySet());

        for (String p : set) {
            if (filesDiffer(from.get(p), to.get(p)))
                throw new CantMergeException(p);
        }

        PersistentTreeMap<String, byte[]> res = to;

        set = new TreeSet<>(from.keySet());
        set.removeAll(to.keySet());
        for (String s : set){
            res = res.assoc(s, from.get(s));
        }

        m_commits.add(res);
        m_currentCommit = a_result.getRevisionNumber();

        emptyStagingArea();
    }

    private void byteArrayToFile(String a_relativePath, byte[] a_contents) throws IOException {
        File f = new File(a_relativePath).getAbsoluteFile();
        f.getParentFile().mkdirs();
        FileUtils.writeByteArrayToFile(f, a_contents);
    }

    private boolean filesDiffer(byte[] a_left, byte[] a_right){
        return !Arrays.equals(a_left, a_right);
    }
}
