package com.github.ljl.jerrymouse.servlet;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * @program: jerry-mouse-round2
 * @description: test servlet api
 * @author: ljl
 * @create: 2024-07-06 10:36
 **/

public class RequestApiServlet extends HttpServlet {
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setAttribute("name1", 11);
        req.setAttribute("name2", "value2");
        System.out.println("AuthType: " + req.getAuthType());
        System.out.println("ContentType:" + req.getContentType());
        System.out.println("CharacterEncoding:" + req.getCharacterEncoding());
        System.out.println("Method:" + req.getMethod());
        System.out.println("URI:" + req.getRequestURI());
        System.out.println("ContentLength " + req.getContentLength());
        System.out.println("ContentLengthLong " + req.getContentLengthLong());
        System.out.println("DispatcherType: " + req.getDispatcherType());
        System.out.println();
        System.out.println("AttributeNames:");
        if (Objects.nonNull(req.getAttributeNames())) {
            List<String> attributeNames = Collections.list(req.getAttributeNames());
            attributeNames.forEach(name -> {
                System.out.println("attribute name:" + name + ", value: " + req.getAttribute(name));
            });
        }
        System.out.println();

        if (Objects.nonNull(req.getParameterNames())) {
            List<String> parameterNames = Collections.list(req.getParameterNames());
            System.out.println("Parameter:");
            parameterNames.forEach(name -> {
                System.out.println(name + " " + req.getParameter(name));
            });
            System.out.println();
            System.out.println("Multi Parameter[]:");
            parameterNames.forEach(name -> {
                System.out.print(name);
                String[] values = req.getParameterValues(name);
                System.out.print(": ");
                Arrays.stream(values).forEach(value -> {
                    System.out.print(value + ",");
                });
                System.out.println();
            });
            System.out.println();
        }

        // auth 先返回null
        System.out.println("parse auth");
        System.out.println("AuthType:" + req.getAuthType());

        testInputStream(req);

        resp.setContentType("application/json");

        String jsonStr = "{\"servlet\":\"request api servlet\",\"status\":200}";
        Writer writer = resp.getWriter();
        writer.write(jsonStr);
        writer.flush();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String body = readFromReader(req);
        ServletOutputStream outputStream = resp.getOutputStream();
        outputStream.println("receive body by reader:");
        outputStream.println(body);
        outputStream.flush();
    }

    private void testInputStream(HttpServletRequest req) throws IOException {
        InputStream inputStream = req.getInputStream();
        System.out.println("parse body by inputStream");
        // 读取并处理文本内容
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
        String line;
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
        }
        System.out.println();
    }

    private String readFromReader(HttpServletRequest req) throws IOException {
        System.out.println("parse body by reader");
        // 读取并处理文本内容
        BufferedReader reader = req.getReader();
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
            sb.append("\r\n");
        }
        return sb.toString();
    }
}
