package com.github.ljl.jerrymouse.support.servlet.dispatcher;

import lombok.Data;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @program: jerry-mouse-round2
 * @description:
 * @author: ljl
 * @create: 2024-07-05 19:36
 **/

@Data
public class RequestDispatcherContext {

    private HttpServletRequest request;

    private HttpServletResponse response;

    public RequestDispatcherContext(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;
    }
}
