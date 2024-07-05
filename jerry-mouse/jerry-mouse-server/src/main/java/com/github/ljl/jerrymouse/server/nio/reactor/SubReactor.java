package com.github.ljl.jerrymouse.server.nio.reactor;

import com.github.ljl.jerrymouse.server.nio.handler.ReadChannelHandler;
import lombok.Setter;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * @program: jerry-mouse-round2
 * @description:
 * @author: ljl
 * @create: 2024-07-05 11:05
 **/

public class SubReactor extends Reactor {

    public static final int REGISTER_QUEUE_SIZE = 5000;

    @Setter
    private SubReactorManager subReactorManager;

    private final Queue<SocketChannel> registerQueue = new ArrayBlockingQueue<>(REGISTER_QUEUE_SIZE);

    protected SubReactor() throws IOException {
    }

    public void addRegisterChannel(SocketChannel channel) {
        registerQueue.add(channel);
    }

    @Override
    public void run() {
        while (true) {
            try {
                registerChannel();
                eventLoopOnce();
            } catch (Exception e) {
                e.printStackTrace();
                break;
            }
        }
    }

    private void registerChannel() throws IOException {
        for (Iterator<SocketChannel> iterator = registerQueue.iterator(); iterator.hasNext(); ) {
            SocketChannel sc = iterator.next();
            iterator.remove();
            SelectionKey selectionKey = sc.register(selector, SelectionKey.OP_WRITE);
            ReadChannelHandler readChannelHandler = new ReadChannelHandler(selectionKey);
            subReactorManager.initHandler(readChannelHandler);
            selectionKey.attach(readChannelHandler);
            logger.debug("register channel success");
        }
    }
}
