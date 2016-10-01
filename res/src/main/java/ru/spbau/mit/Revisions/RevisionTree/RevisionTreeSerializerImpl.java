package ru.spbau.mit.Revisions.RevisionTree;

import com.sun.istack.internal.NotNull;
import ru.spbau.mit.Revisions.Exceptions.IncorrectFileError;

public class RevisionTreeSerializerImpl implements RevisionTreeSerializer {
    @Override
    public void serialize(RevisionTree a_tree, OutputStream a_out) throws IOException {
        ObjectOutputStream out = new ObjectOutputStream(a_out);
        out.writeObject(a_tree);
        out.close();
    }

    @NotNull
    @Override
    public RevisionTree deserialize(InputStream a_in) throws IOException {
        ObjectInputStream in = new ObjectInputStream(a_in);
        RevisionTree retVal = null;
        try {
            retVal = (RevisionTree) in.readObject();
        } catch (ClassNotFoundException e) {
            throw new IncorrectFileError(a_in.toString(), e);
        }
        in.close();
        return retVal;
    }
}
