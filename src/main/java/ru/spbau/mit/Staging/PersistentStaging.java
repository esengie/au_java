package ru.spbau.mit.Staging;

import com.sun.istack.internal.NotNull;
import org.apache.commons.io.FileUtils;
import org.organicdesign.fp.collections.PersistentTreeMap;
import ru.spbau.mit.Paths.SaveDirLocation;
import ru.spbau.mit.Revisions.CommitNodes.CommitNode;
import ru.spbau.mit.Staging.Exceptions.CantMergeException;
import ru.spbau.mit.Staging.Exceptions.CommitNumberConflictRuntimeException;
import ru.spbau.mit.Staging.Exceptions.FileShouldExistException;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

import static ru.spbau.mit.Staging.StagingPathHelpers.*;

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
     * @param root path to working folder
     */
    public PersistentStaging(Path root) throws IOException {
        m_root = root.toAbsolutePath().toString();
        m_staging = PersistentTreeMap.empty();

        new File(m_root + "/" + SaveDirLocation.getFolderName()).mkdirs();
        add(Paths.get("").toAbsolutePath());
        m_commits.add(m_staging);
        m_currentCommit = m_commits.size() - 1;
    }

    @Override
    public void add(Path filePath) throws IOException {
        File file = filePath.toFile().getAbsoluteFile();
        if (!file.exists())
            throw new FileShouldExistException(filePath.toString());

        String relativePath = relativize(m_root,
                filePath.toAbsolutePath().toString());

        if (file.isFile()) {
            m_staging = m_staging.assoc(relativePath, FileUtils.readFileToByteArray(file));
        }
        if (file.isDirectory()) {
            if (!relativePath.isEmpty())
                relativePath = relativePath + "/";
            String relPath = relativePath;

            List<String> filePaths = listAllFilesRecursively(filePath.toAbsolutePath().toString());
            filePaths = filePaths.stream()
                    .map(s -> relPath + s)
                    .collect(Collectors.toList());
            for (String s : filePaths) {
                m_staging = m_staging.assoc(s,
                        FileUtils.readFileToByteArray(new File(s).getAbsoluteFile()));
            }
        }
    }

    @Override
    public void commitToDisk(CommitNode node) {
        if (node.getRevisionNumber() != m_commits.size())
            throw new CommitNumberConflictRuntimeException();

        m_commits.add(m_staging);
        m_currentCommit = m_commits.size() - 1;
    }

    @Override
    public void emptyStagingArea() {
        m_staging = m_commits.get(m_currentCommit);
    }

    @Override
    public void checkout(CommitNode node) throws IOException {
        if (!(node.getRevisionNumber() < m_commits.size()))
            throw new CommitNumberConflictRuntimeException();

        eraseWorkingDir(m_root);

        PersistentTreeMap<String, byte[]> commitContents =
                m_commits.get(node.getRevisionNumber());

        for (Map.Entry<String, byte[]> it : commitContents.entrySet()) {
            byteArrayToFile(m_root + "/" + it.getKey(), it.getValue());
        }

        m_currentCommit = node.getRevisionNumber();
        emptyStagingArea();
    }

    @Override
    public void merge(CommitNode from, CommitNode to, CommitNode result) throws IOException {
        PersistentTreeMap<String, byte[]> fromCommit = m_commits.get(from.getRevisionNumber());
        PersistentTreeMap<String, byte[]> toCommit = m_commits.get(to.getRevisionNumber());

        if (result.getRevisionNumber() != m_commits.size())
            throw new CommitNumberConflictRuntimeException();

        Set<String> set = new TreeSet<>(fromCommit.keySet());
        set.retainAll(toCommit.keySet());

        for (String p : set) {
            if (filesDiffer(fromCommit.get(p), toCommit.get(p)))
                throw new CantMergeException(p);
        }

        PersistentTreeMap<String, byte[]> res = toCommit;

        set = new TreeSet<>(fromCommit.keySet());
        set.removeAll(toCommit.keySet());
        for (String s : set) {
            res = res.assoc(s, fromCommit.get(s));
        }

        m_commits.add(res);
        m_currentCommit = result.getRevisionNumber();

        emptyStagingArea();
    }

    @Override
    public void reset(Path file) throws IOException {
        PersistentTreeMap<String, byte[]> commitContents =
                m_commits.get(m_currentCommit);

        String path = relativize(m_root, file.toString());
        if (!commitContents.containsKey(path))
            throw new FileShouldExistException(path);

        byteArrayToFile(path, commitContents.get(path));
        add(new File(path).getAbsoluteFile().toPath()); // add it again if need be
    }

    @Override
    public void remove(Path file) throws IOException {
        String path = relativize(m_root, file.toString());
        if (!m_staging.containsKey(path))
            throw new FileShouldExistException(path);

        m_staging = m_staging.without(path);
        removeOnDisk(path);

    }

    @NotNull
    @Override
    public RepoStatus status() throws IOException {
        Set<String> allRemoved = formRemoved();
        Set<String> allAdded = formNewlyAdded();
        Set<String> allModifiedAdded = formModifiedAdded();
        Set<String> allModifiedUnAdded = formModifiedUnAdded();
        Set<String> allUntracked = formUntracked();
        return new RepoStatusImpl(allAdded, allModifiedAdded,
                allModifiedUnAdded, allUntracked, allRemoved);
    }

    private Set<String> formUntracked() {
        Set<String> allPresentInRepo = new TreeSet<>(listAllFilesRecursively(m_root));
        Set<String> allTracked = new TreeSet<>(m_staging.keySet());
        allPresentInRepo.removeAll(allTracked);
        return allPresentInRepo;
    }

    private Set<String> formRemoved() {
        PersistentTreeMap<String, byte[]> lastCommit = m_commits.get(m_currentCommit);
        Set<String> allRemoved = new TreeSet<>(lastCommit.keySet());
        allRemoved.removeAll(m_staging.keySet());
        return allRemoved;
    }

    private Set<String> formNewlyAdded() {
        PersistentTreeMap<String, byte[]> lastCommit = m_commits.get(m_currentCommit);
        Set<String> allAdded = new TreeSet<>(m_staging.keySet());
        allAdded.removeAll(lastCommit.keySet());
        return allAdded;
    }

    private Set<String> formModifiedAdded(){
        PersistentTreeMap<String, byte[]> lastCommit = m_commits.get(m_currentCommit);
        Set<String> containedInStagingAndLastCommit = new TreeSet<>(lastCommit.keySet());
        containedInStagingAndLastCommit.retainAll(m_staging.keySet());
        Set<String> allModified = new TreeSet<>();

        for (String s : containedInStagingAndLastCommit){
            if (filesDiffer(lastCommit.get(s), m_staging.get(s))){
                allModified.add(s);
            }
        }

        return  allModified;
    }

    private Set<String> formModifiedUnAdded() throws IOException {
        Set<String> containedInStagingAndLastCommit = new TreeSet<>(listAllFilesRecursively(m_root));
        containedInStagingAndLastCommit.retainAll(m_staging.keySet());
        Set<String> allModified = new TreeSet<>();

        for (String s : containedInStagingAndLastCommit){
            if (filesDiffer(FileUtils.readFileToByteArray(new File(m_root + "/" + s)), m_staging.get(s))){
                allModified.add(s);
            }
        }

        return  allModified;
    }

    private void removeOnDisk(String path) throws IOException {
        File f = new File(path).getAbsoluteFile();
        if (f.isDirectory()) {
            FileUtils.deleteDirectory(f);
        }
        if (f.isFile()) {
            FileUtils.deleteQuietly(f);
        }
    }

    private void byteArrayToFile(String relativePath, byte[] a_contents) throws IOException {
        File f = new File(relativePath).getAbsoluteFile();
        f.getParentFile().mkdirs();
        FileUtils.writeByteArrayToFile(f, a_contents);
    }

    private boolean filesDiffer(byte[] left, byte[] right) {
        return !Arrays.equals(left, right);
    }
}
