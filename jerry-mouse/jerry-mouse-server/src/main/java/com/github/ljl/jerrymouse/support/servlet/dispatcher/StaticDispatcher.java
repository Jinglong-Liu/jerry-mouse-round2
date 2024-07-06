package com.github.ljl.jerrymouse.support.servlet.dispatcher;

import com.github.ljl.jerrymouse.bootstrap.JerryMouseBootStrap;
import com.github.ljl.jerrymouse.server.nio.handler.SocketWriter;
import com.github.ljl.jerrymouse.support.servlet.response.JerryMouseResponse;
import com.github.ljl.jerrymouse.utils.FileUtils;
import com.github.ljl.jerrymouse.utils.HttpUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @program: jerry-mouse-round2
 * @description:
 * @author: ljl
 * @create: 2024-07-05 20:10
 **/

public class StaticDispatcher implements IDispatcher {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Override
    public void dispatch(RequestDispatcherContext context) {
        final HttpServletRequest request = context.getRequest();
        final HttpServletResponse response = context.getResponse();
        final SocketWriter socketWriter = ((JerryMouseResponse)response).getSockerWriter();

        String absolutePath = FileUtils.buildFullPath(
                JerryMouseBootStrap.class.getResource("/").getPath(), request.getRequestURI());
        try {
            String content = new String(Files.readAllBytes(Paths.get(absolutePath)), StandardCharsets.UTF_8);
            logger.info("Static html found, path: {}, content={}", absolutePath, content);
            socketWriter.write(HttpUtils.http200Resp(content));
        } catch (IOException e) {
            logger.error("File not found: {}", absolutePath);
            socketWriter.write(HttpUtils.http404Resp());
        }
    }
}
