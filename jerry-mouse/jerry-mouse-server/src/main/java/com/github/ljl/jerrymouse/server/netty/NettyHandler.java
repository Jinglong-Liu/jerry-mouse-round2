package com.github.ljl.jerrymouse.server.netty;

import com.github.ljl.jerrymouse.server.nio.handler.SocketWriter;
import com.github.ljl.jerrymouse.support.context.ApplicationContextManager;
import com.github.ljl.jerrymouse.support.servlet.dispatcher.RequestDispatcher;
import com.github.ljl.jerrymouse.support.servlet.dispatcher.RequestDispatcherContext;
import com.github.ljl.jerrymouse.support.servlet.request.JerryMouseRequest;
import com.github.ljl.jerrymouse.support.servlet.response.JerryMouseResponse;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.Charset;

/**
 * @program: jerry-mouse-round2
 * @description:
 * @author: ljl
 * @create: 2024-07-05 09:54
 **/

public class NettyHandler extends ChannelInboundHandlerAdapter implements SocketWriter {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private ChannelHandlerContext channelHandlerContext;
    @Override
    public void channelRead(ChannelHandlerContext context, Object msg) {
        this.channelHandlerContext = context;
        ByteBuf buf = (ByteBuf) msg;
        byte[] bytes = new byte[buf.readableBytes()];
        buf.readBytes(bytes);
        String message = new String(bytes, Charset.defaultCharset());
        logger.debug("netty receive request message:\n{}", message);
        // till now, there is only one application, one context

        JerryMouseRequest request = new JerryMouseRequest(message);
        ServletContext servletContext = ApplicationContextManager.getApplicationContext(request);
        request.setServletContext(servletContext);
        // client can use response to write data, so the socketWrite is necessary
        HttpServletResponse response = new JerryMouseResponse(this, servletContext);
        RequestDispatcherContext dispatcherContext = new RequestDispatcherContext(request, response);
        // then, only focus on this
        RequestDispatcher.get().dispatch(dispatcherContext);
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

    @Override
    public void write(String data) {
        ByteBuf responseBuf = Unpooled.copiedBuffer(data, Charset.defaultCharset());
        channelHandlerContext.writeAndFlush(responseBuf)
                .addListener(ChannelFutureListener.CLOSE);
    }

    @Override
    public void write(byte[] data) {
        ByteBuf responseBuf = Unpooled.wrappedBuffer(data);
        channelHandlerContext.writeAndFlush(responseBuf)
                .addListener(ChannelFutureListener.CLOSE);
    }
}
