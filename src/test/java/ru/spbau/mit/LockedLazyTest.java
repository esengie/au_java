package ru.spbau.mit;

import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.Assert.assertEquals;

public class LockedLazyTest {

    @Test
    public void testGet() {
        final int threadCount = 100;

        Lazy<Long> lazy = LazyFactory.getLockedLazy(System::currentTimeMillis);
        ExecutorService executorService = Executors.newCachedThreadPool();

        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    latch.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                assertEquals(lazy.get(), lazy.get());
            });
        }
    }
}
