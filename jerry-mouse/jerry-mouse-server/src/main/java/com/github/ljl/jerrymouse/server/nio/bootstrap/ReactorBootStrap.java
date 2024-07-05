package com.github.ljl.jerrymouse.server.nio.bootstrap;

import com.github.ljl.jerrymouse.server.nio.handler.PipeLineHandler;
import com.github.ljl.jerrymouse.server.nio.pipeline.PipelineInitializer;
import com.github.ljl.jerrymouse.server.nio.reactor.MasterReactor;
import com.github.ljl.jerrymouse.server.nio.reactor.SubReactorManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * @program: jerry-mouse-round2
 * @description:
 * @author: ljl
 * @create: 2024-07-05 13:08
 **/

public class ReactorBootStrap {
    private Logger logger = LoggerFactory.getLogger(getClass());

    private MasterReactor masterReactor;

    private final SubReactorManager subReactorManager = SubReactorManager.get();

    public ReactorBootStrap pipelineInitializer(PipelineInitializer<PipeLineHandler> handleInitializer) {
        subReactorManager.setPipelineInitializer(handleInitializer);
        return this;
    }

    public void bind(InetSocketAddress address) {
        try {
            masterReactor = new MasterReactor();
            masterReactor.bindAndRegister(address);

            String ipAddress = address.getAddress().getHostAddress();
            int port = address.getPort();
            logger.info("start listen on port {}", port);
            logger.info("visit url http://{}:{}", ipAddress, port);
            masterReactor.start();
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("reactor server start failed!");
        }
    }

    private ReactorBootStrap() {

    }

    public static ReactorBootStrap build() {
        return new ReactorBootStrap();
    }
}
