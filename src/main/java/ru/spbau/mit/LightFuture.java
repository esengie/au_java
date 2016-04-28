package ru.spbau.mit;

import java.util.function.Function;

public interface LightFuture<T> {
    T get() throws LightExecutionException;

    boolean isReady();

    <R> LightFuture<R> thenApply(Function<? super T, ? extends R> function);

}
