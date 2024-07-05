package com.github.ljl.jerrymouse.server.nio.handler.http;

import com.github.ljl.jerrymouse.server.nio.handler.ServiceHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;

/**
 * @program: jerry-mouse-round2
 * @description:
 * @author: ljl
 * @create: 2024-07-05 13:06
 **/

public class HttpRequestDecoder implements ServiceHandler<byte[], HttpServletRequest> {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Override
    public HttpServletRequest service(byte[] bytes) {
        String msg = new String(bytes, StandardCharsets.UTF_8);
        logger.info("reactor receive request message:\n" + msg);
        return null;
    }
}
