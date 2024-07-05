package com.github.ljl.jerrymouse.server.netty;

/**
 * @program: jerry-mouse-round2
 * @description:
 * @author: ljl
 * @create: 2024-07-05 14:16
 **/

public class NettyException extends RuntimeException {
    public NettyException(Throwable throwable) {
        super(throwable);
    }
    public NettyException(String message) {
        super(message);
    }
    public NettyException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
