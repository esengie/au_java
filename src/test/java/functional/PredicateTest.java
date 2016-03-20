package functional;

import org.junit.Test;

import static org.junit.Assert.*;

public class PredicateTest {

    @Test
    public void always() {
        assertTrue(Predicate.ALWAYS_TRUE.or(Predicate.ALWAYS_FALSE).apply(10));
        assertFalse(Predicate.ALWAYS_TRUE.and(Predicate.ALWAYS_FALSE).apply(10));
    }

    @Test
    public void or(){
        Predicate<Integer> p1 = x -> x == 12;
        Predicate<Integer> p2 = x -> x == 11;

        assertTrue(p1.or(p2).apply(11));
        assertTrue(p2.or(p1).apply(12));
        assertFalse(p1.or(p1).apply(10));
    }

    @Test
    public void and(){
        Predicate<Integer> p1 = x -> x == 12;
        Predicate<Integer> p2 = x -> x == 11;
        Predicate<Integer> p3 = x -> x > 11;

        assertFalse(p1.and(p2).apply(10));
        assertFalse(p2.and(p1).apply(11));
        assertTrue(p1.and(p1).apply(12));
        assertFalse(p2.and(p3).apply(13));
    }

    @Test
    public void not() throws Exception {
        Predicate<Integer> p = x -> x % 2 == 0;

        assertTrue(p.apply(10));
        assertFalse(p.not().apply(10));
    }

}