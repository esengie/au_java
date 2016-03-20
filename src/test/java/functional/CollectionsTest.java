package functional;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.*;

public class CollectionsTest {

    @Before
    public void init() {
        vec = new ArrayList<>();
        vec.add(1);
        vec.add(2);
        vec.add(3);
        vec.add(4);
        vec.add(3);
        vec.add(5);
    }

    @Test
    public void map() {
        Function1<Integer, Integer> square = value -> value * value;
        Collection<Integer> result = toColl(Collections.map(square, vec));
        Integer[] expected = new Integer[]{1, 4, 9, 16, 9, 25};
        assertEquals(expected.length, result.size());
        assertTrue(result.containsAll(Arrays.asList(expected)));
    }

    @Test
    public void filter() {
        Predicate<Integer> small = val -> val > 3;
        Collection<Integer> result = toColl(Collections.filter(small, vec));
        Integer[] expected = new Integer[]{4, 5};
        assertEquals(expected.length, result.size());
        assertTrue(result.containsAll(Arrays.asList(expected)));
    }

    @Test
    public void takeWhile() {
        Predicate<Integer> small = val -> val < 3;
        Collection<Integer> result = toColl(Collections.takeWhile(small, vec));
        Integer[] expected = new Integer[]{1, 2};
        assertEquals(expected.length, result.size());
        assertTrue(result.containsAll(Arrays.asList(expected)));
    }

    @Test
    public void takeUnless() {
        Predicate<Integer> small = val -> val > 4;
        Collection<Integer> result = toColl(Collections.takeUnless(small, vec));
        Integer[] expected = new Integer[]{1, 2, 3, 4, 3};
        assertEquals(expected.length, result.size());
        assertTrue(result.containsAll(Arrays.asList(expected)));
    }

    @Test
    public void foldl() {
        Function2<Integer, Integer, Integer> f = (a, b) -> a + b;
        int k = Collections.foldl(f, 0, vec);
        assertEquals(18, k);
        f = (a, b) -> a - b;
        k = Collections.foldl(f, 0, vec);
        assertEquals(-18, k);
    }

    @Test
    public void foldr() {
        Function2<Integer, Integer, Integer> f = (a, b) -> a + b;
        int k = Collections.foldr(f, 0, vec);
        assertEquals(18, k);
        f = (a, b) -> a - b;
        k = Collections.foldr(f, 0, vec);
        assertEquals(-4, k);
    }

    private static <T> Collection<T> toColl(Iterable<T> iterable) {
        Collection<T> collection = new ArrayList<>();
        for (T elem : iterable) {
            collection.add(elem);
        }
        return collection;
    }

    private ArrayList<Integer> vec = null;
}