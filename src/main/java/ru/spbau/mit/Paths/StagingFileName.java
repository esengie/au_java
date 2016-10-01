package ru.spbau.mit.Paths;


import ru.spbau.mit.Staging.Staging;

public class StagingFileName {
    private static final String m_name = "staging.asd";

    private StagingFileName() {
    }

    public static String getFileName() {
        return m_name;
    }
}
