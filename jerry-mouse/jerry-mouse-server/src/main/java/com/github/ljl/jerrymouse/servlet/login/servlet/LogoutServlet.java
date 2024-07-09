package com.github.ljl.jerrymouse.servlet.login.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Objects;

/**
 * @program: jerry-mouse-round2
 * @description:
 * @author: ljl
 * @create: 2024-07-08 08:19
 **/

public class LogoutServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (Objects.isNull(session) || session.getAttribute("username") == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"message\":\"Unauthorized. Can't logout\"}");
        } else {
            /**
             * 使之失效，也可以手动设置session-timeout
             */
            session.invalidate();
            response.setContentType("application/json");
            response.getWriter().write("{\"message\":\"Logout successful\"}");
        }
        response.getWriter().flush();
    }
}
