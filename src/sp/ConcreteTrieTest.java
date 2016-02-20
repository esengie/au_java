package sp;

import static org.junit.Assert.assertEquals;

public class ConcreteTrieTest {

    ConcreteTrie tr;

    @org.junit.Before
    public void setUp() throws Exception {
        tr = new ConcreteTrie();
    }

    @org.junit.Test
    public void testAdd() throws Exception {
        assertEquals(true, tr.add("money"));
        assertEquals(false, tr.add("money"));
        assertEquals(true, tr.add("mon"));
        assertEquals(false, tr.add("mon"));
        assertEquals(true, tr.add(""));
    }

    @org.junit.Test
    public void testContains() throws Exception {
        tr.add("money");
        assertEquals(true, tr.contains("money"));
        assertEquals(false, tr.contains("mon"));
        tr.add("mon");
        assertEquals(true, tr.contains("mon"));
    }

    @org.junit.Test
    public void testRemove() throws Exception {
        tr.add("");
        assertEquals(true, tr.remove(""));
        assertEquals(false, tr.remove(""));
        tr.add("money");
        tr.add("mon");
        assertEquals(true, tr.remove("money"));
        assertEquals(true, tr.contains("mon"));
        assertEquals(false, tr.contains("money"));

        assertEquals(false, tr.remove("mono"));
        assertEquals(true, tr.remove("mon"));
    }

    @org.junit.Test
    public void testSize() throws Exception {
        tr.add("money");
        assertEquals(1, tr.size());
        tr.add("mon");
        assertEquals(2, tr.size());
        tr.add("bevvie");
        assertEquals(3, tr.size());
    }

    @org.junit.Test
    public void testHowManyStartsWithPrefix() throws Exception {
        tr.add("money");
        tr.add("mon");
        tr.add("mono");
        assertEquals(3, tr.howManyStartsWithPrefix("mon"));
        tr.add("");
        assertEquals(4, tr.howManyStartsWithPrefix(""));
    }
}