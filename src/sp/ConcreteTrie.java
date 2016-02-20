package sp;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class ConcreteTrie implements Trie {

    public List<ConcreteTrie> nodes = new ArrayList<>();

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
            mSize++;
            return true;
        }
        return addHelper(element, 0);
    }

    private boolean addHelper(String elem, int pos) {
        ConcreteTrie trie = trieOnLevel(elem.charAt(pos));
        boolean added;
        if (trie != null) {
            if (pos == elem.length() - 1) {
                if (trie.mRoot.isTerminal) return false;
                else {
                    trie.mRoot.isTerminal = true;
                    trie.mSize++;
                    mSize++;
                    return true;
                }
            }
            added = trie.addHelper(elem, pos + 1);
            if (added) mSize++;
        } else {
            trie = new ConcreteTrie(elem.charAt(pos));
            nodes.add(trie);
            added = true;
            mSize++;
            if (pos == elem.length() - 1) {
                trie.mRoot.isTerminal = true;
                trie.mSize++;
                return true;
            }
            trie.addHelper(elem, pos + 1);
        }
        return added;
    }

    @Override
    public boolean contains(String element) {
        if (element.length() == 0) return mRoot.isTerminal;
        return containsHelper(element, 0);
    }

    void print() {
        Queue<ConcreteTrie> q = new LinkedList<>();
        Queue<Integer> l = new LinkedList<>();
        q.add(this);
        l.add(0);
        ConcreteTrie t;
        int lev;
        while (q.size() != 0) {
            t = q.poll();
            lev = l.poll();
            System.out.print(String.format("%c%d ", t.mRoot.character, t.size()));
            for (ConcreteTrie f : t.nodes) {
                q.add(f);
            }
            if (l.size() == 0){
                System.out.println();
                lev++;
                for (int i = 0; i < q.size(); ++i){
                    l.add(lev);
                }
            }
        }
        System.out.println();
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
            if (it.mRoot.character == element) {
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
        ConcreteTrie it = trieOnLevel(elem.charAt(pos));
        if (it == null) return false;

        if (pos == elem.length() - 1) {
            if (!it.mRoot.isTerminal) {
                return false;
            }
            if (it.size() > 1) {
                it.mSize--;
                mSize--;
                it.mRoot.isTerminal = false;
                return true;
            }
            nodes.remove(it);
            mSize--;
            return true;
        }
        boolean deleted = it.removeHelper(elem, pos + 1);
        if (!deleted) return false;

        if (it.size() >= 1) {
            mSize--;
            return true;
        }
        nodes.remove(it);
        mSize--;
        return true;
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
        char character;
        boolean isTerminal = false;

        public Node(char a) {
            character = a;
        }
    }
}
