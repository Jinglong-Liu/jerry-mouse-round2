package com.github.ljl.jerrymouse.server.nio.handler;

import com.github.ljl.jerrymouse.server.nio.pipeline.DefaultPipeLine;
import com.github.ljl.jerrymouse.exception.ReactorException;

import java.io.Closeable;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.LinkedList;
import java.util.List;

/**
 * @program: jerry-mouse-round2
 * @description:
 * @author: ljl
 * @create: 2024-07-05 11:33
 **/

public class ReadChannelHandler implements ChannelHandler, PipeLineHandler, Closeable {

    public static final int BUF_SIZE = 1024;
    private final SelectionKey key;
    private final SocketChannel sc;

    private final List<Byte> msg = new LinkedList<>();
    private byte[] bytes;
    private final ByteBuffer buffer = ByteBuffer.allocateDirect(BUF_SIZE);

    private final DefaultPipeLine pipeline = new DefaultPipeLine();

    public ReadChannelHandler(SelectionKey key) {
        this.key = key;
        this.sc = (SocketChannel) key.channel();
    }

    @Override
    public void run() {
        if (isValid() && readChannelComplete()) {
            bytes = new byte[msg.size()];
            for (int i = 0; i < msg.size(); i++) {
                bytes[i] = msg.get(i);
            }

            Object result = pipeline().service(bytes);
            if (result != null) {
                writeChannel((byte[]) result);
            }
            this.close();
        }
    }

    private boolean readChannelComplete() {
        if (!isValid()) {
            return true;
        }
        try {
            synchronized (sc) {
                if (isValid()) {
                    buffer.clear();
                    int len = sc.read(buffer);
                    if (!isValid() || len <= 0) {
                        return true;
                    }
                    buffer.flip();
                    for (int i = 0, limit = buffer.limit(); i < limit; i++) {
                        msg.add(buffer.get(i));
                    }
                    return false;
                }
            }
        } catch (IOException e) {
            throw new ReactorException(e);
        }
        return true;
    }

    private void writeChannel(byte[] response) {
        if (!isValid()) {
            return;
        }
        try {
            synchronized (sc) {
                if (!isValid()) {
                    return;
                }
                sc.write(ByteBuffer.wrap(response));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public DefaultPipeLine pipeline() {
        return pipeline;
    }

    private boolean isValid() {
        return key.isValid() && sc.isConnected();
    }

    @Override
    public void close() {
        try {
            synchronized (sc) {
                key.cancel();
                sc.socket().close();
                sc.close();
            }
        } catch (IOException ignored) {
        }
    }
}
