package com.github.ljl.jerrymouse.support.servlet.manager;

import javax.servlet.http.HttpServlet;
import java.util.HashMap;
import java.util.Map;

/**
 * @program: jerry-mouse-round2
 * @description:
 * @author: ljl
 * @create: 2024-07-05 19:47
 **/

public class ServletManager {
    private Map<String, HttpServlet> servletMap = new HashMap<>();

    public HttpServlet getServlet(String urlPattern) {
        return servletMap.get(urlPattern);
    }

    public void register(String urlPattern, HttpServlet servlet) {
        servletMap.put(urlPattern, servlet);
    }
}
