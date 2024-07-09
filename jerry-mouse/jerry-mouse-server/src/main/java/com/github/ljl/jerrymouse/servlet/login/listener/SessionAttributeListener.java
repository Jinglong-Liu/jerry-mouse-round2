package com.github.ljl.jerrymouse.servlet.login.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;

/**
 * @program: jerry-mouse-round2
 * @description:
 * @author: ljl
 * @create: 2024-07-08 10:51
 **/

public class SessionAttributeListener implements HttpSessionAttributeListener {

    private static Logger logger = LoggerFactory.getLogger(SessionAttributeListener.class);

    @Override
    public void attributeAdded(HttpSessionBindingEvent event) {
        logger.info("session attributeAdded, {} {}:{}", event.getSession().getId(), event.getName(), event.getValue());
    }

    @Override
    public void attributeRemoved(HttpSessionBindingEvent event) {
        logger.info("session attributeRemoved, {} {}:{}", event.getSession().getId(), event.getName(), event.getValue());
    }

    @Override
    public void attributeReplaced(HttpSessionBindingEvent event) {
        logger.info("session attributeReplaced, {} {}:{}", event.getSession().getId(), event.getName(), event.getValue());
    }
}
