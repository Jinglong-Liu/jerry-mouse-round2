package com.github.ljl.jerrymouse.servlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.http.HttpServletRequest;

/**
 * @program: jerry-mouse-round2
 * @description:
 * @author: ljl
 * @create: 2024-07-07 20:59
 **/

public class RequestLifeStyleListener implements ServletRequestListener {
    private static final Logger logger = LoggerFactory.getLogger(RequestLifeStyleListener.class);
    @Override
    public void requestInitialized(ServletRequestEvent sre) {
        HttpServletRequest request = (HttpServletRequest) sre.getServletRequest();
        long startTime = System.currentTimeMillis();
        request.setAttribute("startTime", startTime);
        // 记录请求信息
        logger.info("Request initialized: " + request.getMethod() + " " + request.getRequestURI() + " from " + request.getRemoteAddr());
    }

    @Override
    public void requestDestroyed(ServletRequestEvent sre) {
        HttpServletRequest request = (HttpServletRequest) sre.getServletRequest();
        long startTime = (Long) request.getAttribute("startTime");
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        // 记录请求处理时间
        logger.info("Request destroyed: " + request.getMethod() + " " + request.getRequestURI() + " took " + duration + " ms");
    }
}
