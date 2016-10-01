package ru.spbau.mit.Revisions.Branches;

public class AsdBranchFactory {
    private AsdBranchFactory() {

    }

    public static AsdBranch createBranch(String a_name) {
        return new AsdBranchImpl(a_name);
    }
}
