package com.github.ljl.jerrymouse.servlet;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;

/**
 * @program: jerry-mouse-round2
 * @description:
 * @author: ljl
 * @create: 2024-07-06 13:45
 **/

public class OutputStreamServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        final ServletOutputStream outputStream = resp.getOutputStream();
        outputStream.println("ignore\n");
        resp.resetBuffer();
        outputStream.println("println first line");
        outputStream.println("println second line");
        outputStream.flush();
    }
}
