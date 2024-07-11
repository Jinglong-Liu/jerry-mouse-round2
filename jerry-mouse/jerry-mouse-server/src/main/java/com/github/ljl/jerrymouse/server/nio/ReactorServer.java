package com.github.ljl.jerrymouse.server.nio;

import com.github.ljl.jerrymouse.bootstrap.WebServerBootStrap;
import com.github.ljl.jerrymouse.server.nio.bootstrap.ReactorBootStrap;

import java.net.InetSocketAddress;

/**
 * @program: jerry-mouse-round2
 * @description:
 * @author: ljl
 * @create: 2024-07-05 13:46
 **/

public class ReactorServer implements WebServerBootStrap {
    public static final String HOST = "127.0.0.1";
    @Override
    public void start(int port) {
        ReactorBootStrap reactorBootStrap = ReactorBootStrap.build();
        reactorBootStrap.pipelineInitializer(handle -> handle.pipeline()
        ).bind(new InetSocketAddress(HOST, port));
    }
}
