package com.github.ljl.jerrymouse.server.nio.reactor;

import com.github.ljl.jerrymouse.server.nio.handler.PipeLineHandler;
import com.github.ljl.jerrymouse.server.nio.pipeline.PipelineInitializer;
import com.github.ljl.jerrymouse.utils.ThreadPoolUtils;
import lombok.Setter;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.Executor;

/**
 * @program: jerry-mouse-round2
 * @description:
 * @author: ljl
 * @create: 2024-07-05 11:19
 **/

public class SubReactorManager {
    public static final int SUB_REACTOR_THREAD_SIZE = 10;
    public static final String HOST = "127.0.0.1";

    private final Executor subThreadPool = ThreadPoolUtils.newFixedThreadPool(SUB_REACTOR_THREAD_SIZE, "sub-reactor");

    private SubReactor[] subReactors = new SubReactor[SUB_REACTOR_THREAD_SIZE];
    @Setter
    private PipelineInitializer<PipeLineHandler> pipelineInitializer;
    private int index = 0;

    private static SubReactorManager instance;

    private SubReactorManager() {
        for (int i = 0; i < subReactors.length; i++) {
            try {
                subReactors[i] = new SubReactor();
                subReactors[i].setSubReactorManager(this);
                subThreadPool.execute(subReactors[i]);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public static SubReactorManager get() {
        if (Objects.isNull(instance)) {
            synchronized (SubReactorManager.class) {
                if (Objects.isNull(instance)) {
                    instance = new SubReactorManager();
                }
            }
        }
        return instance;
    }

    public SubReactor nextReactor() {
        SubReactor reactor = subReactors[index];
        index = (index + 1) % SUB_REACTOR_THREAD_SIZE;
        return reactor;
    }

    public void initHandler(PipeLineHandler handler) {
        this.pipelineInitializer.initHandle(handler);
    }
}
