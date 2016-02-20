package sp;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class ConcreteTrie implements Trie {

    public ArrayList<ConcreteTrie> nodes = new ArrayList<>();
    int mSize = 0;
    Node mRoot;

    public ConcreteTrie() {
        mRoot = new Node('/');
    }

    private ConcreteTrie(char a) {
        mRoot = new Node(a);
    }

    @Override
    public boolean add(String element) {
        if (element.length() == 0) {
            mRoot.isTerminal = true;
            mRoot.number = mSize + 1;
            mSize++;
            return true;
        }
        return addHelper(element, 0, mSize + 1);
    }

    private boolean addHelper(String elem, int pos, int num) {
        ConcreteTrie trie = trieOnLevel(elem.charAt(pos));
        boolean added;
        if (trie != null) {
            if (pos == elem.length() - 1) {
                if (trie.mRoot.isTerminal) return false;
                else {
                    trie.mRoot.isTerminal = true;
                    trie.mRoot.number = num;
                    trie.mSize++;
                    mSize++;
                    return true;
                }
            }
            added = trie.addHelper(elem, pos + 1, num);
            if (added) mSize++;
        } else {
            trie = new ConcreteTrie(elem.charAt(pos));
            nodes.add(trie);
            added = true;
            mSize++;
            if (pos == elem.length() - 1) {
                trie.mRoot.isTerminal = true;
                trie.mRoot.number = num;
                trie.mSize++;
                return true;
            }
            trie.addHelper(elem, pos + 1, num);
        }
        return added;
    }

    @Override
    public boolean contains(String element) {
        if (element.length() == 0) return mRoot.isTerminal;
        return containsHelper(element, 0);
    }

    public void printer() {
        Queue<ConcreteTrie> q = new LinkedList<>();
        q.add(this);
        ConcreteTrie t;
        while (q.size() != 0) {
            t = q.poll();
            System.out.print(String.format("%c%d ", t.mRoot.label, t.size()));
            for (ConcreteTrie f : t.nodes) {
                q.add(f);
            }
        }
    }

    private boolean containsHelper(String elem, int pos) {
        if (pos == elem.length()) {
            return mRoot.isTerminal;
        } else {
            ConcreteTrie el = trieOnLevel(elem.charAt(pos));
            return el != null && el.containsHelper(elem, pos + 1);
        }
    }

    private ConcreteTrie trieOnLevel(char element) {
        for (ConcreteTrie it : nodes) {
            if (it.mRoot.label == element) {
                return it;
            }
        }
        return null;
    }

    @Override
    public boolean remove(String element) {
        if (element.length() == 0) {
            if (mRoot.isTerminal) {
                mRoot.isTerminal = false;
                mSize--;
                return true;
            }
            return false;
        }
        return removeHelper(element, 0);
    }

    private boolean removeHelper(String elem, int pos) {
        return false;
    }

    @Override
    public int size() {
        return mSize;
    }

    @Override
    public int howManyStartsWithPrefix(String prefix) {
        if (prefix.length() == 0) return size();
        return prefixHelper(prefix, 0);
    }

    private int prefixHelper(String prefix, int pos) {
        ConcreteTrie it = trieOnLevel(prefix.charAt(pos));
        if (it == null) return 0;
        if (pos == prefix.length() - 1) {
            return it.size();
        } else {
            return it.prefixHelper(prefix, pos + 1);
        }
    }

    private class Node {
        char label;
        boolean isTerminal = false;
        int number = 0;

        public Node(char a) {
            label = a;
        }

        public Node(int b) {
            number = b;
            isTerminal = true;
        }
    }
}
