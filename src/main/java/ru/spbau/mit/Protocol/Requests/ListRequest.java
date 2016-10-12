package ru.spbau.mit.Protocol.Requests;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

public class ListRequest implements Request {
    private final int commandNumber = 1;
    private String path = "";

    public ListRequest(String path) {
        this.path = path;
    }

    @Override
    public String getName() {
        return "list";
    }

    @Override
    public List<String> getArgs() {
        List<String> ret = new ArrayList<>();
        ret.add(path);
        return ret;
    }

    @Override
    public String toString() {
        String retVal = MessageFormat.format("<{0}> <path: {1}>", commandNumber, path);
        return retVal;
    }
}
