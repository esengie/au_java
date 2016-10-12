package ru.spbau.mit.Protocol.Responses;

import java.text.MessageFormat;
import java.util.Arrays;

public class GetResponse implements Response {
    private final long size;
    private final byte[] content;

    public GetResponse(long size, byte[] content) {
        this.size = size;
        this.content = Arrays.copyOf(content, content.length);
    }

    @Override
    public String toString() {
        String retVal = MessageFormat.format("<size: {0}> <content: {1}>", size, content);
        return retVal;
    }

}
