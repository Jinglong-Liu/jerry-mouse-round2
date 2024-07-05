package com.github.ljl.jerrymouse.server.nio.pipeline;

import com.github.ljl.jerrymouse.server.nio.handler.PipeLineHandler;

/**
 * @program: jerry-mouse-round2
 * @description:
 * @author: ljl
 * @create: 2024-07-05 12:36
 **/

public interface PipelineInitializer<P extends PipeLineHandler> {
    void initHandle(P handle);
}
