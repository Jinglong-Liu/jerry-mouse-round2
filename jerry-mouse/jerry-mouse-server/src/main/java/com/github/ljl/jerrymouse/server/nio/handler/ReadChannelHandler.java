package com.github.ljl.jerrymouse.server.nio.handler;

import com.github.ljl.jerrymouse.server.nio.pipeline.DefaultPipeLine;
import com.github.ljl.jerrymouse.exception.ReactorException;
import com.github.ljl.jerrymouse.support.context.ApplicationContextManager;
import com.github.ljl.jerrymouse.support.servlet.dispatcher.RequestDispatcher;
import com.github.ljl.jerrymouse.support.servlet.dispatcher.RequestDispatcherContext;
import com.github.ljl.jerrymouse.support.servlet.request.JerryMouseRequest;
import com.github.ljl.jerrymouse.support.servlet.response.JerryMouseResponse;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Closeable;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;

/**
 * @program: jerry-mouse-round2
 * @description:
 * @author: ljl
 * @create: 2024-07-05 11:33
 **/

public class ReadChannelHandler implements ChannelHandler, PipeLineHandler, Closeable, SocketWriter {

    private Logger logger = LoggerFactory.getLogger(getClass());
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
            // msg = bytes[]
            String message = new String(bytes, StandardCharsets.UTF_8);
            if (message.isEmpty()) {
                logger.error("empty message!");
                this.close();
                return;
            }
            logger.debug("receive request message:\n{}", message);
            // till now, there is only one application, one context
            ServletContext servletContext = ApplicationContextManager.getApplicationContext();
            HttpServletRequest request = new JerryMouseRequest(message, servletContext);
            // client can use response to write data, so the socketWrite is necessary
            HttpServletResponse response = new JerryMouseResponse(this, servletContext);
            RequestDispatcherContext dispatcherContext = new RequestDispatcherContext(request, response);
            // then, only focus on this
            RequestDispatcher.get().dispatch(dispatcherContext);

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

    @Override
    public void write(byte[] data) {
        writeChannel(data);
    }

    @Override
    public void write(String data) {
        write(data.getBytes(StandardCharsets.UTF_8));
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
