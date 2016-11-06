package ru.spbau.mit.Common;

import ru.spbau.mit.Protocol.RemoteFile;
import ru.spbau.mit.TorrentClient.TorrentFile.FileManager;
import ru.spbau.mit.TorrentClient.TorrentFile.TorrentFileLocal;

import java.io.File;
import java.util.Arrays;
import java.util.HashSet;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class WithFileManager {

    public static FileManager getFileManager(){
        FileManager fm = mock(FileManager.class);

        when(fm.getFileIds()).thenReturn(Arrays.asList(0, 1));
        when(fm.getTorrentFile(0))
        return fm;
    }

    public static TorrentFileLocal getTorrentFileLocal(int id){
        TorrentFileLocal fl = mock(TorrentFileLocal.class);
        when(fl.partSize(0)).thenReturn(RemoteFile.PART_SIZE);
        when(fl.partSize(1)).thenReturn(334);
        when(fl.getParts()).thenReturn(new HashSet<>(Arrays.asList(0,1)));
        when(fl.


        return fl;
    }
}
