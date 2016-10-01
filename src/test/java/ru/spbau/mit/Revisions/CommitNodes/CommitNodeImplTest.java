package ru.spbau.mit.Revisions.CommitNodes;

import org.junit.Test;
import ru.spbau.mit.Revisions.Branches.AsdBranch;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CommitNodeImplTest {
    @Test
    public void equals() throws Exception {
        AsdBranch b = mock(AsdBranch.class);
        when(b.getName()).thenReturn("master");

        CommitNode c = new CommitNodeImpl(b, 12, "we");
        CommitNode c2 = new CommitNodeImpl(b, 12, "we");

        assertEquals(c, c2);
        assertEquals(c.hashCode(), c2.hashCode());
    }

}