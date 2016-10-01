package ru.spbau.mit.Staging;


import com.sun.istack.internal.NotNull;
import ru.spbau.mit.Revisions.Exceptions.IncorrectFileRuntimeException;

import java.io.*;

public class PersistentStagingSerializer implements StagingSerializer {

    @Override
    public void serialize(Staging a_staging, OutputStream a_out) throws IOException {
        ObjectOutputStream out = new ObjectOutputStream(a_out);
        out.writeObject(a_staging);
        out.close();
    }

    @NotNull
    @Override
    public Staging deserialize(InputStream a_in) throws IOException {
        ObjectInputStream in = new ObjectInputStream(a_in);
        Staging retVal = null;
        try {
            retVal = (Staging) in.readObject();
        } catch (ClassNotFoundException e) {
            throw new IncorrectFileRuntimeException(a_in.toString(), e);
        }
        in.close();
        return retVal;
    }
}
