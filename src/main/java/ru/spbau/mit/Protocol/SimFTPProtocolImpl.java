package ru.spbau.mit.Protocol;

import org.apache.commons.io.FileUtils;
import ru.spbau.mit.Protocol.Requests.GetRequest;
import ru.spbau.mit.Protocol.Requests.ListRequest;
import ru.spbau.mit.Protocol.Requests.Request;
import ru.spbau.mit.Protocol.Responses.GetResponse;
import ru.spbau.mit.Protocol.Responses.ListResponse;
import ru.spbau.mit.Protocol.Responses.Response;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

public class SimFTPProtocolImpl implements SimFTPProtocol {

    @Override
    public byte[] formListRequest(String path) {
        Request r = new ListRequest(path);
        return r.toBytes();
    }

    @Override
    public byte[] formGetRequest(String path) {
        Request r = new GetRequest(path);
        return r.toBytes();
    }

    @Override
    public Response readResponse(byte[] contents) {
        return null;
    }

    private Request parseRequest(byte[] input) {
        ByteBuffer bb = ByteBuffer.wrap(input);
        bb.getChar();
        return null;
    }

    @Override
    public Response formResponse(byte[] request) throws IOException {
        Request r = parseRequest(request);
        switch (r.getName()) {
            case "get":
                return formGetResponse(r.getArgs().get(0));
            case "list":
                return formListResponse(r.getArgs().get(0));
        }
        throw new IllegalStateException("Can't be here");
    }

    private Response formListResponse(String path) {
        File f = new File(path);
        if (!f.exists()) {
            return new ListResponse(0);
        }
        if (f.isFile()) {
            return new ListResponse(1, path, false);
        }
        return new ListResponse(f.listFiles().length, path, true);
    }

    private Response formGetResponse(String path) throws IOException {
        File f = new File(path);
        if (!f.exists()){
            return new GetResponse(0, null);
        }
        byte[] bytes = FileUtils.readFileToByteArray(f);
        return new GetResponse(bytes.length, bytes);
    }
}
