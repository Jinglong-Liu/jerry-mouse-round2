package com.github.ljl.jerrymouse.support.servlet.manager;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

/**
 * @program: jerry-mouse-round2
 * @description:
 * @author: ljl
 * @create: 2024-07-07 18:24
 **/

public class ListenerManager {
    private final Set<EventListener> listeners = new LinkedHashSet<>();

    public ListenerManager() {}

    /**
     * add listener(不重复)
     * @param listener
     */
    public synchronized void register(EventListener listener) {
        listeners.add(listener);
    }

    /**
     * @return listeners
     */
    public List<EventListener> getListeners() {
        return new ArrayList<>(listeners);
    }
}
