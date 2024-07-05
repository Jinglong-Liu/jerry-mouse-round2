package com.github.ljl.jerrymouse.server.nio.handler.http;

import com.github.ljl.jerrymouse.server.nio.handler.ServiceHandler;

import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;


/**
 * @program: jerry-mouse-round2
 * @description:
 * @author: ljl
 * @create: 2024-07-05 14:41
 **/

public class HttpResponseEncoder implements ServiceHandler<HttpServletResponse, byte[]> {
    @Override
    public byte[] service(HttpServletResponse response) {
        String result = "HTTP/1.1 200 OK\n" +
                "Content-Type: text/plain\n" +
                "\n" +
                "Hello, Reactor!";
        return result.getBytes(StandardCharsets.UTF_8);
    }
}
