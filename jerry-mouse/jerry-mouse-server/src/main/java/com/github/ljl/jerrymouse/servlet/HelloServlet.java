package com.github.ljl.jerrymouse.servlet;

import com.github.ljl.jerrymouse.support.servlet.response.JerryMouseResponse;
import com.github.ljl.jerrymouse.utils.HttpUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @program: jerry-mouse-round2
 * @description:
 * @author: ljl
 * @create: 2024-07-06 08:01
 **/

public class HelloServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (resp instanceof JerryMouseResponse) {
            JerryMouseResponse response = (JerryMouseResponse) resp;
            response.getSockerWriter().write(HttpUtils.http200Resp("Hello servlet!"));
        }
    }
}
