package ru.spbau.mit;

import org.junit.Test;

import java.lang.reflect.Array;
import java.util.*;

import static org.junit.Assert.*;

public class ThreadPoolImplTest {

    @Test
    public void testShutdown() throws Exception {
        ThreadPool th = new ThreadPoolImpl(5);
        List<LightFuture<Thread>> ls = new LinkedList<>();
        for (int i = 0; i < 5; ++i) {
            ls.add(th.add(ThreadPoolImplTest::helperTestN));
        }
        th.shutdown();
    }


    @Test
    public void testN() throws Exception {
        ThreadPool th = new ThreadPoolImpl(5);
        List<LightFuture<Thread>> ls = new LinkedList<>();
        Set<Thread> st = new HashSet<>();
        for (int i = 0; i < 5; ++i) {
            ls.add(th.add(ThreadPoolImplTest::helperTestN));
        }
        for (int i = 0; i < 5; ++i) {
            st.add(ls.get(i).get());
        }
        assertTrue(st.size() == 5);
    }

    @Test
    public void testAfter() throws Exception {
        ThreadPool th = new ThreadPoolImpl(2);
        LightFuture<String> res = th.add(ThreadPoolImplTest::helperTestDiff);
        Queue<String> results = new LinkedList<>();
        LightFuture<String> res2 = res.thenApply(s -> {
            results.add("slow"); return s.concat("After");
        });
        res2.thenApply(s -> {
            results.add("slower"); return s.concat("After");
        });
        th.add(() -> {
            results.add("fast"); return "Else";
        });
        th.add(() -> {
            results.add("fast"); return "Else";
        });
        th.add(() -> {
            results.add("fast"); return "Else";
        });
        th.add(() -> {
            results.add("fast"); return "Else";
        });
        assertEquals("BeforeAfter", res2.get());
        assertArrayEquals(new String[] {"fast", "fast", "fast", "fast", "slow", "slower"}, results.toArray());
    }


    private static Thread helperTestN(){
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
        }
        return Thread.currentThread();
    }
    private static String helperTestDiff(){
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
        }
        return "Before";
    }
    private static Thread helper2(Thread t){
        System.out.println(t);
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
        }
        return Thread.currentThread();
    }
}