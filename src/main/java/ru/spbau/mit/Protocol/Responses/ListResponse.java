package ru.spbau.mit.Protocol.Responses;

import java.text.MessageFormat;

public class ListResponse implements Response {
    private final int size;
    private String filename = "";
    private boolean is_dir = false;


    public ListResponse(int size) {
        this.size = size;
    }

    public ListResponse(int size, String filename, boolean is_dir) {
        this(size);
        this.filename = filename;
        this.is_dir = is_dir;
    }

    @Override
    public String toString() {
        String retVal = MessageFormat.format("<size: {0}>", size);
        if (size > 0) {
            retVal += MessageFormat.format(" <name: {0}> <is_dir: {1}>", filename, is_dir);
        }
        return retVal;
    }

}
