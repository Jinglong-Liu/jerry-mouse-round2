package com.github.ljl.jerrymouse.server.nio.pipeline;

import com.github.ljl.jerrymouse.server.nio.handler.ServiceHandler;

import java.util.LinkedList;
import java.util.List;

/**
 * @program: jerry-mouse-round2
 * @description:
 * @author: ljl
 * @create: 2024-07-05 12:31
 **/

public class DefaultPipeLine implements ServiceHandler<byte[], byte[]> {
    private final List<ServiceHandler<?, ?>> pipeline = new LinkedList<>();

    public DefaultPipeLine addLast(ServiceHandler<?, ?> handler) {
        pipeline.add(handler);
        return this;
    }

    @Override
    public byte[] service(byte[] msg) {
        Object message =  msg;
        for (ServiceHandler handler : pipeline) {
            message = handler.service(message);
        }
        return (byte[]) message;
    }
}
