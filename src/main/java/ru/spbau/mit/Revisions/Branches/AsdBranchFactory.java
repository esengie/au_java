package ru.spbau.mit.Revisions.Branches;

public class AsdBranchFactory {
    private AsdBranchFactory() {

    }

    public static AsdBranch createBranch(String name) {
        return new AsdBranchImpl(name);
    }
}
