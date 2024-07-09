package com.github.ljl.jerrymouse.servlet.login.servlet;

import com.github.ljl.jerrymouse.utils.TimeUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * @program: jerry-mouse-round2
 * @description:
 * @author: ljl
 * @create: 2024-07-08 08:19
 **/

public class LoginServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        // 假设用户名和密码都是 "admin"，实际应用中应该进行更复杂的验证
        if ("admin".equals(username) && "admin".equals(password)) {
            // 没有就创建一个session
            HttpSession session = request.getSession();
            session.setMaxInactiveInterval(20); // 设置会话超时时间为20秒
            session.setAttribute("username", "user");
            session.setAttribute("username", username);
            session.removeAttribute("username");
            session.setAttribute("username", username);
            response.setContentType("application/json");

            String setCookie = "JSESSIONID=" + session.getId() + ";";
            if (session.getMaxInactiveInterval() > 0) {
                long expireTime = session.getCreationTime() + session.getMaxInactiveInterval() * 1000L;
                String expire = TimeUtils.format(expireTime);
                setCookie += " Expires=" + expire + ";";
            }
            session.setAttribute("username", username);
            response.setHeader("Set-Cookie", setCookie);
            response.getWriter().write("{\"message\":\"Login successful\", \"sessionId\":\"" + session.getId() + "\"}");
        } else {
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"message\":\"Invalid credentials\"}");
        }
        response.getWriter().flush();
    }
}
