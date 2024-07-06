package com.github.ljl.jerrymouse.support.servlet.request;

import com.sun.org.apache.regexp.internal.RE;
import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: jerry-mouse-round2
 * @description:
 * @author: ljl
 * @create: 2024-07-05 17:17
 **/

@Data
public class RequestData {
    private String requestMessage;

    private Map<String, String> headers;

    private Map<String, String[]> parameters;

    private String body;

    private String uri;

    private String method;

    private String requestURI;

    public RequestData(String requestMessage) {
        this.requestMessage = requestMessage;
        this.headers = RequestParser.parseHeaders(requestMessage);
        this.parameters = RequestParser.parseQueryParams(requestMessage);
        this.body = RequestParser.parseRequestBody(requestMessage);
        RequestParser.parseRequestLine(requestMessage, this);
    }
}
