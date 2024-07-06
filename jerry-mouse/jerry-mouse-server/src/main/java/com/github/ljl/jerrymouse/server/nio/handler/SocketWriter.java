package com.github.ljl.jerrymouse.server.nio.handler;

public interface SocketWriter {
    void write(byte[] data);
    void write(String data);
}
