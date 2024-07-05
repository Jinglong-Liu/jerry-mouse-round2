package com.github.ljl.jerrymouse.server.nio.handler;

import com.github.ljl.jerrymouse.server.nio.pipeline.DefaultPipeLine;

/**
 * @program: jerry-mouse-round2
 * @description:
 * @author: ljl
 * @create: 2024-07-05 12:35
 **/

public interface PipeLineHandler {
    DefaultPipeLine pipeline();
}
