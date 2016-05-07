package ru.spbau.mit;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.RunnableFuture;
import java.util.function.Function;

public class LightFutureImpl<T> implements LightFuture<T> {
    public LightFutureImpl(ThreadPool tp) {
        threadPool = tp;
    }

    @Override
    public synchronized T get() throws LightExecutionException, InterruptedException {
        while (value == null) {
            wait();
        }
        if (excepted) {
            throw new LightExecutionException();
        }
        return value;
    }

    synchronized void setRes(T val) {
        value = val;
        notify();
        int i = 0;
        for (Runnable tsk : toDo) {
            threadPool.put(tsk);
        }
    }

    synchronized void excepted() {
        excepted = true;
    }

    @Override
    public synchronized boolean isReady() {
        return value != null && !excepted;
    }

    @Override
    public synchronized <R> LightFuture<R> thenApply(Function<? super T, ? extends R> function) {
        if (isReady()) {
            return threadPool.add(() -> function.apply(value));
        } else {
            LightFutureImpl<R> future = new LightFutureImpl<>(threadPool);
            toDo.add(() -> {
                try {
                    future.setRes(function.apply(this.get()));
                } catch (Exception e) {
                    future.excepted();
                }
            });
            return future;
        }
    }

    private T value = null;
    private Boolean excepted = false;
    private final ThreadPool threadPool;
    private List<Runnable> toDo = new LinkedList<>();
}
