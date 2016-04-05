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

        Predicate<String> equals10 = "10"::equals;
        Predicate<Object> equals12 = "12"::equals;
        assertTrue(equals10.or(equals12).apply("12"));

        Predicate<Function1<Integer, Integer>> check_not_null = f -> f.apply(1) == 1;
        Predicate<Function1<Integer, Integer>> check_null = f -> f.apply(0) == 1;
        Function1<Integer, Integer> unsafe = a -> 1/a;
        assertTrue(check_not_null.or(check_null).apply(unsafe));
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

        Predicate<String> equals10 = "10"::equals;
        Predicate<Object> equals12 = "12"::equals;
        assertFalse(equals10.and(equals12).apply("12"));

        Predicate<Function1<Integer, Integer>> check_not_null = f -> f.apply(1) != 1;
        Predicate<Function1<Integer, Integer>> check_null = f -> f.apply(0) == 1;
        Function1<Integer, Integer> unsafe = a -> 1/a;
        assertFalse(check_not_null.and(check_null).apply(unsafe));
    }

    @Test
    public void not() throws Exception {
        Predicate<Integer> p = x -> x % 2 == 0;

        assertTrue(p.apply(10));
        assertFalse(p.not().apply(10));
    }

}