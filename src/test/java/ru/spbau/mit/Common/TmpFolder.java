package ru.spbau.mit.Common;

import org.junit.Rule;
import org.junit.rules.TemporaryFolder;

public class TmpFolder {
    @Rule
    public final TemporaryFolder folder = new TemporaryFolder();

}
