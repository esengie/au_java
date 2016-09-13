package ru.spbau.mit;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class SingleThreadedLazyTest {

    @Test
    public void testGet() {
        Lazy<Long> lazy = LazyFactory.getSingleLazy(System::currentTimeMillis);
        assertEquals(lazy.get(), lazy.get());
    }

}