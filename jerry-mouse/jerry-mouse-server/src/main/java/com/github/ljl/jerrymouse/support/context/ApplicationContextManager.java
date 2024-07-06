package com.github.ljl.jerrymouse.support.context;

import com.github.ljl.jerrymouse.servlet.HelloServlet;

/**
 * @program: jerry-mouse-round2
 * @description:
 * @author: ljl
 * @create: 2024-07-05 19:58
 **/

public class ApplicationContextManager {
    private static ApplicationContext applicationContext;

    static {
        applicationContext = new ApplicationContext();
        applicationContext.registerServlet("/hello", new HelloServlet());
    }
    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }
}
