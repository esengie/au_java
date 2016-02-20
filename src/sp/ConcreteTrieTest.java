package sp;

import static org.junit.Assert.assertEquals;

public class ConcreteTrieTest {

    Trie trie;

    @org.junit.Before
    public void setUp() throws Exception {
        trie = new ConcreteTrie();
    }

    @org.junit.Test
    public void testAdd() throws Exception {
        assertEquals(true, trie.add("money"));
        assertEquals(false, trie.add("money"));
        assertEquals(true, trie.add("mon"));
        assertEquals(false, trie.add("mon"));
        assertEquals(true, trie.add(""));
    }

    @org.junit.Test
    public void testContains() throws Exception {
        trie.add("money");
        assertEquals(true, trie.contains("money"));
        assertEquals(false, trie.contains("mon"));
        trie.add("mon");
        assertEquals(true, trie.contains("mon"));
    }

    @org.junit.Test
    public void testRemove() throws Exception {
        trie.add("");
        assertEquals(true, trie.remove(""));
        assertEquals(false, trie.remove(""));
        trie.add("money");
        trie.add("mon");
        assertEquals(true, trie.remove("money"));
        assertEquals(true, trie.contains("mon"));
        assertEquals(false, trie.contains("money"));
        assertEquals(false, trie.remove("mono"));
        assertEquals(true, trie.remove("mon"));
    }

    @org.junit.Test
    public void testPrint() throws Exception {
        ConcreteTrie testTree = new ConcreteTrie();
        testTree.add("chicken");
        testTree.add("Sammich");
        testTree.add("chick");
        testTree.add("lol");
        testTree.print();
    }

    @org.junit.Test
    public void testSize() throws Exception {
        trie.add("money");
        assertEquals(1, trie.size());
        trie.add("mon");
        assertEquals(2, trie.size());
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