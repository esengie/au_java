package ru.spbau.mit.Protocol;

import ru.spbau.mit.Protocol.Requests.GetRequest;
import ru.spbau.mit.Protocol.Requests.ListRequest;
import ru.spbau.mit.Protocol.Requests.Request;
import ru.spbau.mit.Protocol.Responses.Response;

import java.util.List;

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
        return null;
    }

    @Override
    public Response formResponse(byte[] request) {
        Request r = parseRequest(request);
        switch (r.getName()){
            case "get":
                return formGetResponse(r.getArgs());
            case "list":
                return formListResponse(r.getArgs());
        }
        throw new IllegalStateException("Can't be here");
    }

    private Response formListResponse(List<String> args) {
        return null;
    }

    private Response formGetResponse(List<String> args) {
        return null;
    }
}
