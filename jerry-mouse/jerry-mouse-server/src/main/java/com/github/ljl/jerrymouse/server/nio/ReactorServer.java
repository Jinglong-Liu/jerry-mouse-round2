package com.github.ljl.jerrymouse.server.nio;

import com.github.ljl.jerrymouse.bootstrap.WebServerBootStrap;
import com.github.ljl.jerrymouse.server.nio.bootstrap.ReactorBootStrap;
import com.github.ljl.jerrymouse.server.nio.handler.http.HttpRequestDecoder;
import com.github.ljl.jerrymouse.server.nio.handler.http.HttpResponseEncoder;
import com.github.ljl.jerrymouse.server.nio.handler.http.HttpServiceHandler;

import java.net.InetSocketAddress;

/**
 * @program: jerry-mouse-round2
 * @description:
 * @author: ljl
 * @create: 2024-07-05 13:46
 **/

public class ReactorServer implements WebServerBootStrap {
    public static final String HOST = "127.0.0.1";
    public static final int PORT = 8888;
    @Override
    public void start() {
        ReactorBootStrap reactorBootStrap = ReactorBootStrap.build();
        reactorBootStrap.pipelineInitializer(handle -> handle.pipeline()
                        .addLast(new HttpRequestDecoder())              //http解码器
                        .addLast(new HttpServiceHandler())              //http业务处理
                        .addLast(new HttpResponseEncoder())             //http编码器
        ).bind(new InetSocketAddress(HOST, PORT));
    }
}
