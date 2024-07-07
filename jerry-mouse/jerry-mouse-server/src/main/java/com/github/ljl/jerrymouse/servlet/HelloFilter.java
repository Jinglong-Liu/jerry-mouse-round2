package com.github.ljl.jerrymouse.servlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import java.io.IOException;

/**
 * @program: jerry-mouse-round2
 * @description:
 * @author: ljl
 * @create: 2024-07-07 16:01
 **/

public class HelloFilter implements Filter {
    private static final Logger logger = LoggerFactory.getLogger(HelloFilter.class);
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        logger.info("Before Hello Filter");
        chain.doFilter(request, response);
        logger.info("After Hello Filter");
    }
}
