package com.github.ljl.jerrymouse.servlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;

/**
 * @program: jerry-mouse-round2
 * @description:
 * @author: ljl
 * @create: 2024-07-07 21:52
 **/

public class ListenerServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // request attribute
        req.setAttribute("name1", "value1");
        req.setAttribute("name1", "value2");
        req.setAttribute("name1", "value2");
        req.removeAttribute("name1");
        req.setAttribute("name1", "value2");
        // context attribute
        ServletContext servletContext = req.getServletContext();
        servletContext.setAttribute("name1", "value1");
        servletContext.setAttribute("name1", "value2");
        servletContext.setAttribute("name1", "value2");
        servletContext.removeAttribute("name1");
        servletContext.setAttribute("name1", "value2");
        // return
        Writer writer = resp.getWriter();
        writer.write("Listener Servlet");
        writer.flush();
    }
}
