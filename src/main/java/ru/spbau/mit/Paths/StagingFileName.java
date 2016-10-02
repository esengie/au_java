package ru.spbau.mit.Paths;

/**
 * Returns the name of the file where the Staging is stored inside the program folder
 * <p>
 * Comments on how to improve this are welcome and expected
 */
public class StagingFileName {
    private static final String m_name = "staging.asd";

    private StagingFileName() {
    }

    public static String getFileName() {
        return m_name;
    }
}
