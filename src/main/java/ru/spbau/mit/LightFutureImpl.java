package ru.spbau.mit;

import java.util.function.Function;

public class LightFutureImpl<T> implements LightFuture<T> {
    public LightFutureImpl(ThreadPool tp) {
        threadPool = tp;
    }

    @Override
    public synchronized T get() throws LightExecutionException {
        try {
            while (value == null) {
                wait();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new LightExecutionException();
        }
        if (excepted) {
            throw new LightExecutionException();
        }
        return value;
    }

    synchronized void setRes(T val) {
        value = val;
        notify();
    }

    void excepted() {
        excepted = true;
    }

    @Override
    public synchronized boolean isReady() {
        return value != null;
    }

    @Override
    public synchronized <R> LightFuture<R> thenApply(Function<? super T, ? extends R> function) {
        return threadPool.add(() -> {         // to make sure this is called only after the initial one has finished
            try {
                return function.apply(this.get());
            } catch (LightExecutionException e) {
                throw new RuntimeException();
            }
        });
    }

    private T value = null;
    private Boolean excepted = false;
    private final ThreadPool threadPool;
}
