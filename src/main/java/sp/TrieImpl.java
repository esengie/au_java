package sp;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class TrieImpl implements Trie, StreamSerializable {

    private final List<TrieImpl> mNodes = new ArrayList<>();

    private int mSize = 0;
    private final Node mRoot;

    public TrieImpl() {
        mRoot = new Node('/');
    }

    private TrieImpl(char a) {
        mRoot = new Node(a);
    }

    @Override
    public boolean add(String element) {
        if (element.isEmpty()) {
            mRoot.isTerminal = true;
            mSize++;
            return true;
        }
        return addHelper(element, 0);
    }

    private boolean addHelper(String elem, int pos) {
        TrieImpl trie = findChildren(elem.charAt(pos));
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
            trie = new TrieImpl(elem.charAt(pos));
            mNodes.add(trie);
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
        if (element.isEmpty()) return mRoot.isTerminal;
        return containsHelper(element, 0);
    }

    private boolean containsHelper(String elem, int pos) {
        if (pos == elem.length()) {
            return mRoot.isTerminal;
        } else {
            TrieImpl el = findChildren(elem.charAt(pos));
            return el != null && el.containsHelper(elem, pos + 1);
        }
    }

    private TrieImpl findChildren(char element) {
        for (TrieImpl it : mNodes) {
            if (it.mRoot.mCharacter == element) {
                return it;
            }
        }
        return null;
    }

    @Override
    public boolean remove(String element) {
        if (element.isEmpty()) {
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
        TrieImpl it = findChildren(elem.charAt(pos));
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
            mNodes.remove(it);
            mSize--;
            return true;
        }
        boolean deleted = it.removeHelper(elem, pos + 1);
        if (!deleted) return false;

        if (it.size() >= 1) {
            mSize--;
            return true;
        }
        mNodes.remove(it);
        mSize--;
        return true;
    }

    @Override
    public int size() {
        return mSize;
    }

    @Override
    public int howManyStartsWithPrefix(String prefix) {
        if (prefix.isEmpty()) return size();
        return prefixHelper(prefix, 0);
    }

    private int prefixHelper(String prefix, int pos) {
        TrieImpl it = findChildren(prefix.charAt(pos));
        if (it == null) return 0;
        if (pos == prefix.length() - 1) {
            return it.size();
        } else {
            return it.prefixHelper(prefix, pos + 1);
        }
    }

    void print(StringBuilder os) {
        os.append(String.format("%c%d%b", mRoot.mCharacter, size(), mRoot.isTerminal));
        for (TrieImpl f : mNodes) {
            f.print(os);
        }
        os.append('|');
    }


    @Override
    public void serialize(OutputStream out) throws IOException {
        DataOutputStream os = new DataOutputStream(out);
        flush(os);
        os.flush();
    }

    private void flush(DataOutputStream out) throws IOException {
        out.writeChar(mRoot.mCharacter);
        out.writeInt(size());
        out.writeBoolean(mRoot.isTerminal);
        for (TrieImpl f : mNodes) {
            f.flush(out);
        }
        out.writeChar('|');
    }

    @Override
    public void deserialize(InputStream in) throws IOException {
        mNodes.clear();
        DataInputStream is = new DataInputStream(in);
        mRoot.mCharacter = is.readChar();
        mSize = is.readInt();
        mRoot.isTerminal = is.readBoolean();
        restore(is);
    }

    private void restore(DataInputStream is) throws IOException {
        while(is.available() != 0) {
            char ch = is.readChar();
            if (ch == '|')
                return;
            TrieImpl trie = new TrieImpl();
            trie.mSize = is.readInt();
            trie.mRoot.mCharacter = ch;
            trie.mRoot.isTerminal = is.readBoolean();
            mNodes.add(trie);
            trie.restore(is);
        }
    }

    private static class Node {
        private char mCharacter;
        private boolean isTerminal = false;

        public Node(char a) {
            mCharacter = a;
        }
    }
}
