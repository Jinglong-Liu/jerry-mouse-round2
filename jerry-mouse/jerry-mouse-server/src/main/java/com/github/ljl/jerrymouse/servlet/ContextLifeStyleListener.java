package com.github.ljl.jerrymouse.servlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * @program: jerry-mouse-round2
 * @description:
 * @author: ljl
 * @create: 2024-07-07 20:25
 **/

public class ContextLifeStyleListener implements ServletContextListener {

    private final Logger logger = LoggerFactory.getLogger(ContextLifeStyleListener.class);

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        logger.info("Local servletContext Initialized");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        logger.info("Local servletContext Destroyed");
    }
}
