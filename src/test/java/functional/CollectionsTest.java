package functional;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

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
        Collection<Integer> result = Collections.map(square, vec);
        assertEquals(Arrays.asList(1, 4, 9, 16, 9, 25), result);

        vec.add(null);
        Collection<Boolean> result2 = Collections.map(not_null, vec);
        assertEquals(Arrays.asList(true, true, true, true, true, true, false), result2);

    }

    @Test
    public void filter() {
        Predicate<Integer> small = val -> val > 3;
        Collection<Integer> result = Collections.filter(small, vec);
        assertEquals(Arrays.asList(4,5), result);

        vec.add(null);
        Collection<Integer> result2 = Collections.filter(not_null, vec);
        assertEquals(Arrays.asList(1,2,3,4,3,5), result2);
    }

    @Test
    public void takeWhile() {
        Predicate<Integer> small = val -> val < 3;
        Collection<Integer> result = Collections.takeWhile(small, vec);
        assertEquals(Arrays.asList(1,2), result);

        vec.add(null);
        Collection<Integer> result2 = Collections.takeWhile(not_null, vec);
        assertEquals(Arrays.asList(1,2,3,4,3,5), result2);
    }

    @Test
    public void takeUnless() {
        Predicate<Integer> small = val -> val > 4;
        Collection<Integer> result = Collections.takeUnless(small, vec);
        assertEquals(Arrays.asList(1,2,3,4,3), result);

        vec.add(null);
        Collection<Integer> result2 = Collections.takeUnless(not_null.not(), vec);
        assertEquals(Arrays.asList(1,2,3,4,3,5), result2);
    }

    @Test
    public void foldl() {
        Function2<Integer, Integer, Integer> f = (a, b) -> a + b;
        int k = Collections.foldl(f, 0, vec);
        assertEquals(18, k);
        f = (a, b) -> a - b;
        k = Collections.foldl(f, 0, vec);
        assertEquals(-18, k);

        Function2<Boolean, Object, Boolean> foldl_not_null1 = (l, r) -> l && r != null;

        vec.add(null);
        boolean indicator = Collections.foldl(foldl_not_null1, true, vec);
        assertFalse(indicator);
    }

    @Test
    public void foldr() {
        Function2<Integer, Integer, Integer> f = (a, b) -> a + b;
        int k = Collections.foldr(f, 0, vec);
        assertEquals(18, k);
        f = (a, b) -> a - b;
        k = Collections.foldr(f, 0, vec);
        assertEquals(-4, k);

        Function2<Object, Boolean, Boolean> foldr_not_null2 = (l, r) -> l != null && r;

        vec.add(null);
        boolean indicator = Collections.foldr(foldr_not_null2, true, vec);
        assertFalse(indicator);
    }

    private static final Predicate<Object> not_null = x -> x != null;
    private List<Integer> vec = null;
}