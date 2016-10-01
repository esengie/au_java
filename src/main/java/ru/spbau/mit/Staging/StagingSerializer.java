package ru.spbau.mit.Staging;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface StagingSerializer {
    void serialize(Staging a_staging, OutputStream a_out) throws IOException;
    Staging deserialize(InputStream a_in) throws IOException;
}
