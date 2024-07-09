package com.github.ljl.jerrymouse.servlet.login.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 * @program: jerry-mouse-round2
 * @description:
 * @author: ljl
 * @create: 2024-07-08 10:51
 **/

public class SessionLifeStyleListener implements HttpSessionListener {

    private static Logger logger = LoggerFactory.getLogger(SessionLifeStyleListener.class);

    @Override
    public void sessionCreated(HttpSessionEvent se) {
        logger.warn("sessionCreated: {}", se.getSession().getId());
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        logger.warn("sessionDestroyed: {}", se.getSession().getId());
    }
}
