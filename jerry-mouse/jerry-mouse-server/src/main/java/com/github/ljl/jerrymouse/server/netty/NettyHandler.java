package com.github.ljl.jerrymouse.server.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;

/**
 * @program: jerry-mouse-round2
 * @description:
 * @author: ljl
 * @create: 2024-07-05 09:54
 **/

public class NettyHandler extends ChannelInboundHandlerAdapter {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void channelRead(ChannelHandlerContext context, Object msg) {
        ByteBuf buf = (ByteBuf) msg;
        byte[] bytes = new byte[buf.readableBytes()];
        buf.readBytes(bytes);
        String requestString = new String(bytes, Charset.defaultCharset());
        logger.info("netty receive request message:\n" + requestString);

        String result = "HTTP/1.1 200 OK\n" +
                "Content-Type: text/plain\n" +
                "\n" +
                "Hello, Netty!";

        ByteBuf responseBuf = Unpooled.copiedBuffer(result, Charset.defaultCharset());
        context.writeAndFlush(responseBuf)
                .addListener(ChannelFutureListener.CLOSE);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext context) {
        context.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext context, Throwable cause) {
        logger.error("NettyHandler cause exception");
        context.close();
        throw new NettyException(cause);
    }
}
