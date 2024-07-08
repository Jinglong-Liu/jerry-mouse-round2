package com.github.ljl.jerrymouse.servlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContextAttributeEvent;
import javax.servlet.ServletContextAttributeListener;

/**
 * @program: jerry-mouse-round2
 * @description:
 * @author: ljl
 * @create: 2024-07-07 18:22
 **/

public class ContextAttributeListener implements ServletContextAttributeListener {

    private final Logger logger = LoggerFactory.getLogger(ContextAttributeListener.class);

    @Override
    public void attributeAdded(ServletContextAttributeEvent event) {
        logger.info("Context attributeAdded {}:{}", event.getName(), event.getValue());
    }

    @Override
    public void attributeRemoved(ServletContextAttributeEvent event) {
        logger.info("Context attributeRemoved {}:{}", event.getName(), event.getValue());
    }

    @Override
    public void attributeReplaced(ServletContextAttributeEvent event) {
        logger.info("Context attributeReplaced {}:{}", event.getName(), event.getValue());
    }
}
