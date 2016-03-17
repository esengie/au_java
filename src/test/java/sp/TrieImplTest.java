package sp;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class TrieImplTest {

    private Trie trie;

    @org.junit.Before
    public void setUp() throws Exception {
        trie = new TrieImpl();
    }

    @org.junit.Test
    public void testAdd() throws Exception {
        assertTrue(trie.add("money"));
        assertFalse(trie.add("money"));
        assertTrue(trie.add("mon"));
        assertFalse(trie.add("mon"));
        assertTrue(trie.add(""));
    }

    @org.junit.Test
    public void testContains() throws Exception {
        trie.add("money");
        assertTrue(trie.contains("money"));
        assertFalse(trie.contains("mon"));
        trie.add("mon");
        assertTrue(trie.contains("mon"));
    }

    @org.junit.Test
    public void testRemove() throws Exception {
        trie.add("");
        assertTrue(trie.remove(""));
        assertFalse(trie.remove(""));
        trie.add("money");
        trie.add("mon");
        assertTrue(trie.remove("money"));
        assertTrue(trie.contains("mon"));
        assertFalse(trie.contains("money"));
        assertFalse(trie.remove("mono"));
        assertTrue(trie.remove("mon"));
    }

    @org.junit.Test
    public void testPrint() throws Exception {
        TrieImpl testTree = new TrieImpl();
        testTree.add("chicken");
        testTree.add("Sammich");
        testTree.add("Sammich");
        testTree.add("chick");
        testTree.add("lol");
        testTree.add("chickon");
        testTree.remove("chick");

        StringBuilder s = new StringBuilder();
        testTree.print(s);

        System.out.println(s.toString());

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        testTree.serialize(outputStream);

        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        testTree.deserialize(inputStream);

        StringBuilder s2 = new StringBuilder();
        testTree.print(s2);

        System.out.println(s2.toString());
    }

    @org.junit.Test
    public void testSize() throws Exception {
        trie.add("money");
        assertEquals(1, trie.size());
        trie.add("mon");
        assertEquals(2, trie.size());
        trie.add("bevvie");
        assertEquals(3, trie.size());
        trie.add("bevvie");
        assertEquals(3, trie.size());
    }

    @org.junit.Test
    public void testHowManyStartsWithPrefix() throws Exception {
        trie.add("money");
        trie.add("mon");
        trie.add("mono");
        assertEquals(3, trie.howManyStartsWithPrefix("mon"));
        trie.add("");
        assertEquals(4, trie.howManyStartsWithPrefix(""));
    }
}