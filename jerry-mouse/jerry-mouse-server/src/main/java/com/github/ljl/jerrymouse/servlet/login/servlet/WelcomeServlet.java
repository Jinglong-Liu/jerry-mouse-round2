package com.github.ljl.jerrymouse.servlet.login.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * @program: jerry-mouse-round2
 * @description:
 * @author: ljl
 * @create: 2024-07-08 08:22
 **/

public class WelcomeServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        HttpSession session = request.getSession(false);
        response.setContentType("application/json");
        if (session == null || session.getAttribute("username") == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"message\":\"Unauthorized\"}");
        } else {
            String username = (String) session.getAttribute("username");
            response.getWriter().write("{\"message\":\"SessionId checked! Welcome, " + username + "\"}");
        }
        response.getWriter().flush();
    }
}
