package ru.spbau.mit.Serialization;

import ru.spbau.mit.Revisions.Exceptions.IncorrectFileRuntimeException;

import java.io.*;

public class SerializerImpl<T> implements Serializer<T> {
    @Override
    public void serialize(T tree, OutputStream out) throws IOException {
        ObjectOutputStream output = new ObjectOutputStream(out);
        output.writeObject(tree);
        output.close();
    }

    @Override
    public T deserialize(InputStream in) throws IOException {
        ObjectInputStream input = new ObjectInputStream(in);
        T retVal = null;
        try {
            retVal = (T) input.readObject();
        } catch (ClassNotFoundException e) {
            throw new IncorrectFileRuntimeException(in.toString(), e);
        }
        input.close();
        return retVal;
    }
}
