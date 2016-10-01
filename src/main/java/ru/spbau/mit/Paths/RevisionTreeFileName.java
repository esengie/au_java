package ru.spbau.mit.Paths;


/**
 * Returns the name of the file where the Revision tree is stored inside the program folder
 *
 * Comments on how to improve this are welcome and expected
 */
public class RevisionTreeFileName {
    private static final String m_name = "revTree.asd";
    private RevisionTreeFileName(){
    }
    public static String getFileName(){
        return m_name;
    }
}
