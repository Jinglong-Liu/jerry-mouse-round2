package com.github.ljl.jerrymouse.support.servlet.dispatcher;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * @program: jerry-mouse-round2
 * @description:
 * @author: ljl
 * @create: 2024-07-05 20:07
 **/

public class RequestDispatcher implements IDispatcher{

    private static RequestDispatcher instance;
    private ServletDispatcher servletDispatcher = new ServletDispatcher();
    private StaticDispatcher staticDispatcher = new StaticDispatcher();
    private EmptyDispatcher emptyDispatcher = new EmptyDispatcher();

    private RequestDispatcher() {
    }

    public static RequestDispatcher get() {
        if (Objects.isNull(instance)) {
            synchronized (RequestDispatcher.class) {
                if (Objects.isNull(instance)) {
                    instance = new RequestDispatcher();
                }
            }
        }
        return instance;
    }
    @Override
    public void dispatch(RequestDispatcherContext context) {
        IDispatcher dispatcher = matchDispatcher(context);
        dispatcher.dispatch(context);
    }

    private IDispatcher matchDispatcher(RequestDispatcherContext context) {
        HttpServletRequest request = context.getRequest();
        String uri = request.getRequestURI();
        if (Objects.isNull(uri)) {
            return emptyDispatcher;
        } else if(uri.endsWith(".html") || uri.endsWith("htm")) {
            return staticDispatcher;
        }
        return servletDispatcher;
    }
}
