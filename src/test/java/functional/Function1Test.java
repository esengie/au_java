package functional;

import org.junit.Test;

import static org.junit.Assert.*;

public class Function1Test {
    @Test
    public void compose() {
        Function1<Double, Double> square = value -> value * value;
        Function1<Double, Double> sqrt = value -> Math.sqrt(value);

        assertEquals(2, square.compose(sqrt).apply(2.).intValue());
        assertEquals(1, sqrt.compose(sqrt).apply(2.).intValue());

        Function1<Object, Boolean> notNull = x -> x != null;
        Function1<Integer, Integer> toNull = x -> null;
        assertFalse(toNull.compose(notNull).apply(10));
    }
}
