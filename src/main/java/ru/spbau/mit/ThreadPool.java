package ru.spbau.mit;

import java.util.function.Supplier;

public interface ThreadPool {

    <T> LightFuture<T> add(Supplier<T> task);

    void put(Runnable task);
    void shutdown();
}
