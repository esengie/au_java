package ru.spbau.mit.Revisions.RevisionTree;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import ru.spbau.mit.Revisions.Branches.AsdBranch;
import ru.spbau.mit.Revisions.Branches.AsdBranchFactory;
import ru.spbau.mit.Revisions.CommitNodes.CommitNode;
import ru.spbau.mit.Revisions.CommitNodes.CommitNodeFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import static org.junit.Assert.assertEquals;

public class RevisionTreeSerializerImplTest {

    @Rule
    public final TemporaryFolder folder = new TemporaryFolder();

    @Test
    public void serialize() throws Exception {
        File f = folder.newFile();

        RevisionTree tree = new RevisionTreeImpl();

        AsdBranch b1 = AsdBranchFactory.createBranch("lol");
        AsdBranch b2 = AsdBranchFactory.createBranch("slave");

        tree.branchCreate(b1);
        tree.checkout(b1);

        for (int i = 0; i < 10; ++i) {
            CommitNode c = CommitNodeFactory.createNode(tree, "asd");
            if (i == 5) {
                tree.branchCreate(b2);
                tree.checkout(b2);
            }
            if (i == 7){
                tree.checkout(b1);
            }
            tree.commit(c);
        }

    RevisionTreeSerializer ser = new RevisionTreeSerializerImpl();

    FileOutputStream out = new FileOutputStream(f);
        ser.serialize(tree,out);
        out.close();

    RevisionTree tree2 = ser.deserialize(new FileInputStream(f));

    assertEquals(tree2, tree);
}

}