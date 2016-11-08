package ru.spbau.mit.Common;

import org.apache.commons.io.FileUtils;
import org.junit.rules.TemporaryFolder;
import ru.spbau.mit.Protocol.RemoteFile;
import ru.spbau.mit.TorrentClient.TorrentFile.FileManager;

import java.io.File;

public class WithFileManager extends TemporaryFolder {
    FileManager fm;
    File resources;
    public File curDir;

    public WithFileManager(File resources){
        this.resources = resources;
    }

    @Override
    protected void before() throws Throwable {
        super.before();
        File tmp = newFolder();
        curDir = tmp.getParentFile();
        FileUtils.deleteDirectory(tmp);
        fm = new FileManager(curDir);
        FileUtils.copyDirectory(resources, curDir);
        int i = 0;
        for (File f : curDir.listFiles()){
            fm.addTorrentFile(f, new RemoteFile(i, f.getName(), f.length()));
            ++i;
        }
    }

    public FileManager getFileManager(){
        return fm;
    }

}
