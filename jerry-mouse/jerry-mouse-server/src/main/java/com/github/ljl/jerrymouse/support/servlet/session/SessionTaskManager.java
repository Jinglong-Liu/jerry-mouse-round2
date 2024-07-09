package com.github.ljl.jerrymouse.support.servlet.session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @program: jerry-mouse-round2
 * @description: listener
 * @author: ljl
 * @create: 2024-07-08 12:14
 **/

public class SessionTaskManager {

    private final Logger logger = LoggerFactory.getLogger(SessionTaskManager.class);
    private final Timer timer;
    private final Map<String, TimerTask> sessionTaskMap;

    private static SessionTaskManager instance;

    private SessionTaskManager() {
        this.timer = new Timer(true); // 使用守护线程的定时器
        this.sessionTaskMap = new ConcurrentHashMap<>();
    }

    public void sessionCreated(HttpSessionWrapper session) {
        sessionTimeReset(session);
    }

    public static SessionTaskManager get() {
        if (Objects.isNull(instance)) {
            synchronized (SessionTaskManager.class) {
                if (Objects.isNull(instance)) {
                    instance = new SessionTaskManager();
                }
            }
        }
        return instance;
    }

    public void sessionTimeReset(HttpSessionWrapper session) {
        cancelTask(session);
        long timeout = session.calculateTimeout();
        if (timeout >= 0) {
            TimerTask sessionTask = new SessionTimeoutTask(session);
            sessionTaskMap.put(session.getId(), sessionTask);
            // 启动定时任务，检查会话是否超时
            timer.schedule(sessionTask, timeout);
        }
    }

    // 如果有定时任务，取消相应定时任务
    void cancelTask(HttpSessionWrapper session) {
        TimerTask oldSessionTask = sessionTaskMap.get(session.getId());
        if (Objects.nonNull(oldSessionTask)) {
            oldSessionTask.cancel();
        }
    }

    private class SessionTimeoutTask extends TimerTask {
        private final HttpSessionWrapper session;

        public SessionTimeoutTask(HttpSessionWrapper session) {
            this.session = session;
        }

        @Override
        public void run() {
            if (Objects.nonNull(session)) {
                // 已经不合法，直接删除
                if (!session.isValid()) {
                    sessionTaskMap.remove(session.getId());
                }
                // 本来合法，现在超时
                else {
                    logger.info("session timeout: {}", session.getId());
                    session.invalidate();
                    sessionTaskMap.remove(session.getId());
                }
            }
        }
    }
}
