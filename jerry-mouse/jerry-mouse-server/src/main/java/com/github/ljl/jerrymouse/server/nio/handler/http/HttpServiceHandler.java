package com.github.ljl.jerrymouse.server.nio.handler.http;

import com.github.ljl.jerrymouse.server.nio.handler.ServiceHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @program: jerry-mouse-round2
 * @description:
 * @author: ljl
 * @create: 2024-07-05 14:41
 **/

public class HttpServiceHandler implements ServiceHandler<HttpServletRequest, HttpServletResponse> {
    @Override
    public HttpServletResponse service(HttpServletRequest request) {
        return null;
    }
}
