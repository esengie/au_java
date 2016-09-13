package ru.spbau.mit;

import java.util.function.Supplier;

public class LazyFactory<T> {
    public static <T> Lazy<T> getSingleLazy(Supplier<T> supplier) {
        return new SingleThreadedLazy<>(supplier);
    }

    public static <T> Lazy<T> getLockedLazy(Supplier<T> supplier) {
        return new LockedLazy<>(supplier);
    }

    public static <T> Lazy<T> getLockFreeLazy(Supplier<T> supplier) {
        return new LockFreeLazy<>(supplier);
    }


}
