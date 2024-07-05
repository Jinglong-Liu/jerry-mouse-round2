package com.github.ljl.jerrymouse.server.nio.reactor;

import com.github.ljl.jerrymouse.server.nio.handler.AcceptChannelHandler;
import com.github.ljl.jerrymouse.utils.ThreadPoolUtils;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.util.concurrent.Executor;

/**
 * @program: jerry-mouse-round2
 * @description:
 * @author: ljl
 * @create: 2024-07-05 11:05
 **/

public class MasterReactor extends Reactor {
    private ServerSocketChannel ssc;
    private static final Executor threadPool = ThreadPoolUtils.newFixedThreadPool(1, "master-reactor");
    private AcceptChannelHandler acceptHandler;

    public MasterReactor() throws IOException {
    }

    @Override
    protected void eventLoopOnce() {
        acceptHandler.run();
    }

    public void bindAndRegister(InetSocketAddress bindAddress) throws IOException {
        ssc = ServerSocketChannel.open();
        ssc.socket().bind(bindAddress);
        acceptHandler = new AcceptChannelHandler(ssc);
    }

    public void start() {
        threadPool.execute(this);
    }
}
