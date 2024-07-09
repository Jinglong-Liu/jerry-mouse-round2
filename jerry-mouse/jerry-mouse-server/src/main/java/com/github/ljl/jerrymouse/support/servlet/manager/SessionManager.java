package com.github.ljl.jerrymouse.support.servlet.manager;

import javax.servlet.http.HttpSession;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @program: jerry-mouse-round2
 * @description:
 * @author: ljl
 * @create: 2024-07-08 19:23
 **/

public class SessionManager {
    private Map<String, HttpSession> sessionMap = new ConcurrentHashMap<>();

    public void addSession(HttpSession session) {
        sessionMap.put(session.getId(), session);
    }
    public void changeSessionId(HttpSession session, String oldId) {
        sessionMap.remove(oldId);
        sessionMap.put(session.getId(), session);
    }
    public HttpSession getSession(String sessionId) {
        return sessionMap.get(sessionId);
    }

    public void removeSession(String sessionId) {
        sessionMap.remove(sessionId);
    }
    public Enumeration<HttpSession> getSessions() {
        return Collections.enumeration(sessionMap.values());
    }
}
