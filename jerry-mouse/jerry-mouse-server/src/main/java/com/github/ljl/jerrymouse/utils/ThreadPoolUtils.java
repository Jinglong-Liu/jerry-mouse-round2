package com.github.ljl.jerrymouse.utils;

import com.github.ljl.jerrymouse.exception.ThreadPoolException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

/**
 * @program: jerry-mouse-round2
 * @description:
 * @author: ljl
 * @create: 2024-07-05 09:30
 **/

public class ThreadPoolUtils {

    public static Executor newFixedThreadPool(int nThreads, String name) {
        return Executors.newFixedThreadPool(nThreads, new NamedThreadFactory(name));
    }

    static class NamedThreadFactory implements ThreadFactory {

        private static final int MAX_THREAD_COUNTS = 2048;
        private static Map<String, Integer> nameMap = new HashMap<>();

        private String name;
        private int maxThreadCount;
        public NamedThreadFactory(String name) {
            this(name, 100);
        }
        public NamedThreadFactory(String name, int maxThreadCount) {
            if (maxThreadCount > MAX_THREAD_COUNTS) {
                throw new ThreadPoolException("maxThreadCount over " + MAX_THREAD_COUNTS);
            }
            this.maxThreadCount = maxThreadCount;
            this.name = name;
            nameMap.put(name, nextCount(nameMap.getOrDefault(name, 0)));
        }
        @Override
        public Thread newThread(Runnable r) {
            int count = nameMap.get(name);
            nameMap.put(name, nextCount(count));
            return new Thread(r, this.name + "-" + count);
        }
        private int nextCount(int count) {
            return (count + 1) % maxThreadCount;
        }
    }
}
