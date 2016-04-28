package ru.spbau.mit;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

class Worker extends Thread {
    Worker(SynchQueue<Runnable> queue) {
        tasks = queue;
    }

    @Override
    public void run() {
        try {
            while (!isInterrupted()) {
                Runnable task = tasks.take();
                task.run();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private final SynchQueue<Runnable> tasks;
}

public class ThreadPoolImpl implements ThreadPool {
    public ThreadPoolImpl(int size) {
        tasks = new SynchQueue<>(size * 2);
        for (int i = 0; i < size; i++) {
            Worker w = new Worker(tasks);
            workers.add(w);
        }
        workers.forEach(Worker::start);
    }

    @Override
    public <T> LightFuture<T> add(Supplier<T> task) {
        LightFutureImpl<T> future = new LightFutureImpl<>(this);
        try {
            tasks.put(() -> {
                try {
                    future.setRes(task.get());
                } catch (Exception e) {
                    future.excepted();
                }
            });
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return future;
    }

    @Override
    public void shutdown() {
        try {
            for (Worker w: workers) {
                w.interrupt();
                tasks.put(()->Thread.currentThread().interrupt()); // Help, doesn't work without this,
                // probably because only one thread is inside synchronised in queue,
                // so interrupt works only for that one(?)
            }
            for (Worker w: workers) {
                w.join();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private final SynchQueue<Runnable> tasks;
    private final List<Worker> workers = new ArrayList<>();
}
