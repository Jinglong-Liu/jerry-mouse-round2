package com.github.ljl.jerrymouse.support.context;

import com.github.ljl.jerrymouse.servlet.HelloServlet;
import com.github.ljl.jerrymouse.servlet.OutputStreamServlet;
import com.github.ljl.jerrymouse.servlet.RequestApiServlet;
import com.github.ljl.jerrymouse.servlet.WriterServlet;

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
    }
    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }
}
