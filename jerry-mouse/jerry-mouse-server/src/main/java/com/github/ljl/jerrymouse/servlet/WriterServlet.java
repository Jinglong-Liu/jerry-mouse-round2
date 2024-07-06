package com.github.ljl.jerrymouse.servlet;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;

/**
 * @program: jerry-mouse-round2
 * @description: test writer
 * @author: ljl
 * @create: 2024-07-06 13:36
 **/

public class WriterServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        final Writer writer = resp.getWriter();
        writer.write("ignore\n");
        resp.resetBuffer();
        writer.write("write first line\n");
        writer.write("write second line\n");
        writer.flush();
    }
}
