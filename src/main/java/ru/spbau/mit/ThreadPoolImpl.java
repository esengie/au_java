package ru.spbau.mit;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.function.Supplier;

class Worker extends Thread {
    Worker(Queue<Runnable> queue) {
        tasks = queue;
    }

    @Override
    public void run() {
        try {
            while (!isInterrupted()) {
                Runnable task;
                while (tasks.isEmpty()) {
                    tasks.wait();
                }
                synchronized (tasks) {
                    if (tasks.isEmpty()) continue;
                    task = tasks.peek();
                    tasks.remove();
                }
                task.run();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    private final Queue<Runnable> tasks;
}

public class ThreadPoolImpl implements ThreadPool {
    public ThreadPoolImpl(int size) {
        for (int i = 0; i < size; i++) {
            Worker w = new Worker(tasks);
            workers.add(w);
        }
        workers.forEach(Worker::start);
    }

    @Override
    public synchronized <T> LightFuture<T> add(Supplier<T> task) {
        LightFutureImpl<T> future = new LightFutureImpl<>(this);
        tasks.add(() -> {
            try {
                future.setRes(task.get());
            } catch (Exception e) {
                future.excepted();
            }
        });
        tasks.notifyAll();
        return future;
    }

    @Override
    public void shutdown() {
        try {
            for (Worker w : workers) {
                w.interrupt();
                w.join();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private final Queue<Runnable> tasks = new LinkedList<>();
    private final List<Worker> workers = new ArrayList<>();
}
