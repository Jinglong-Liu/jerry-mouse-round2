package com.github.ljl.jerrymouse.server.nio.handler;

public interface ServiceHandler<T, E> {
    E service(T msg);
}
