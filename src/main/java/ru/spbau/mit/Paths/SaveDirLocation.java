package ru.spbau.mit.Paths;

/**
 * Returns the name of the folder where the things are stored (a-la .git)
 *
 * Comments on how to improve this are welcome and expected
 */
public class SaveDirLocation {
    private static final String m_name = ".asd";
    private SaveDirLocation(){}
    public static String getFolderName(){
        return m_name;
    }
}
