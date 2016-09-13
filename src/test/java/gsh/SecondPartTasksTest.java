package gsh;

import com.google.common.collect.ImmutableMap;
import org.junit.Test;

import java.io.UncheckedIOException;
import java.util.*;

import static org.junit.Assert.*;
import static gsh.SecondPartTasks.*;

public class SecondPartTasksTest {

    @Test(expected = UncheckedIOException.class)
    public void testFindQuotesFileNotFound() {
        List<String> paths = Arrays.asList("file", "file1");
        SecondPartTasks.findQuotes(paths, "abc");
    }

    @Test
    public void testFindQuotes() {
        List<String> paths = Arrays.asList(
                "src/test/resources/file1",
                "src/test/resources/file2",
                "src/test/resources/file3"
        );
        List<String> result = Arrays.asList(
                "We've known each other for so long",
                "We've known each other for so long"
        );

        assertEquals(result, findQuotes(paths, "known"));

        assertEquals(Collections.emptyList(), findQuotes(paths, "find me if you can"));
    }

    @Test
    public void testPiDividedBy4() {
        double x = piDividedBy4();
        assertTrue(x > 0.7);
        assertTrue(x < 0.8);
    }

    @Test
    public void testFindPrinter() {
        Map<String, List<String>> authors = new HashMap<>();
        authors.put("Hello", Arrays.asList("Hello", "world"));
        assertEquals("Hello", findPrinter(authors));

        assertEquals(null, findPrinter(Collections.emptyMap()));
    }

    @Test
    public void testCalculateGlobalOrder() {
        Map<String, Integer> result = calculateGlobalOrder(ORDERS);

        assertEquals(5, result.size());
        assertEquals(5, result.get("res1").intValue());
        assertEquals(0, result.get("res2").intValue());
        assertEquals(15, result.get("res3").intValue());
        assertEquals(4, result.get("res4").intValue());
        assertEquals(12, result.get("res5").intValue());
    }

    private static final List<Map<String, Integer>> ORDERS =
            Arrays.asList(
                    ImmutableMap.of(
                            "res1", 3,
                            "res2", 0,
                            "res3", 5,
                            "res4", 4
                    ),
                    ImmutableMap.of(
                            "res1", 2,
                            "res3", 10,
                            "res5", 12
                    )
            );
}