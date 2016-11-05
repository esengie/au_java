package ru.spbau.mit.TorrentClient;

import ru.spbau.mit.TorrentClient.TorrentFile.TorrentFileLocal;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

class FileToLeech {
    final int fileId;
    final TorrentFileLocal file;
    final Set<Integer> partsNeeded;
    final Map<InetSocketAddress, Set<Integer>> seedParts = new ConcurrentHashMap<>();
    final Queue<InetSocketAddress> seeds = new ConcurrentLinkedQueue<>();

    FileToLeech(int fileId, Set<Integer> partIds, TorrentFileLocal file) {
        this.fileId = fileId;
        this.partsNeeded = partIds;
        this.file = file;
    }
}
