package com.github.ljl.jerrymouse.servlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRequestAttributeEvent;
import javax.servlet.ServletRequestAttributeListener;
import javax.servlet.http.HttpServletRequest;

/**
 * @program: jerry-mouse-round2
 * @description:
 * @author: ljl
 * @create: 2024-07-07 21:17
 **/

public class RequestAttributeListener implements ServletRequestAttributeListener {
    private final Logger logger = LoggerFactory.getLogger(RequestAttributeListener.class);

    @Override
    public void attributeAdded(ServletRequestAttributeEvent srae) {
        HttpServletRequest request = (HttpServletRequest) srae.getServletRequest();
        logger.info("Request attributeAdded: {}, {}:{}", request.getRequestURI(), srae.getName(), srae.getValue());
    }

    @Override
    public void attributeRemoved(ServletRequestAttributeEvent srae) {
        HttpServletRequest request = (HttpServletRequest) srae.getServletRequest();
        logger.info("Request attributeRemoved: {}, {}:{}", request.getRequestURI(), srae.getName(), srae.getValue());
    }

    @Override
    public void attributeReplaced(ServletRequestAttributeEvent srae) {
        HttpServletRequest request = (HttpServletRequest) srae.getServletRequest();
        logger.info("Request attributeReplaced: {}, {}:{}", request.getRequestURI(), srae.getName(), srae.getValue());
    }
}
