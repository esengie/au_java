package ru.spbau.mit;

import java.util.LinkedList;
import java.util.Queue;

public class SynchQueue<T> {
    private Queue<T> queue = new LinkedList<T>();
    private int capacity;

    public SynchQueue(int capacity) {
        this.capacity = capacity;
    }

    public synchronized void put(T element) throws InterruptedException {
        while(queue.size() == capacity) {
            wait();
        }
        queue.add(element);
        notifyAll();
    }

    public synchronized T take() throws InterruptedException {
        while(queue.isEmpty()) {
            wait();
        }
        T item = queue.remove();
        notifyAll();
        return item;
    }
}
