package ru.spbau.mit.Serialization;

import com.sun.istack.internal.NotNull;
import ru.spbau.mit.Revisions.Exceptions.IncorrectFileRuntimeException;
import ru.spbau.mit.Revisions.RevisionTree.RevisionTree;

import java.io.*;

public class SerializerImpl<T> implements Serializer<T> {
    @Override
    public void serialize(T a_tree, OutputStream a_out) throws IOException {
        ObjectOutputStream out = new ObjectOutputStream(a_out);
        out.writeObject(a_tree);
        out.close();
    }

    @NotNull
    @Override
    public T deserialize(InputStream a_in) throws IOException {
        ObjectInputStream in = new ObjectInputStream(a_in);
        T retVal = null;
        try {
            retVal = (T) in.readObject();
        } catch (ClassNotFoundException e) {
            throw new IncorrectFileRuntimeException(a_in.toString(), e);
        }
        in.close();
        return retVal;
    }
}
