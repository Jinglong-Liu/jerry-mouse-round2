package com.github.ljl.jerrymouse.support.servlet.session;

import com.github.ljl.jerrymouse.support.context.ApplicationContext;
import lombok.Data;

import java.util.Collections;
import java.util.Enumeration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @program: jerry-mouse-round2
 * @description:
 * @author: ljl
 * @create: 2024-07-08 16:57
 **/

@Data
public class HttpSessionData {

    private HttpSessionWrapper session;

    private ApplicationContext applicationContext;

    private Map<String, Object> attributes = new ConcurrentHashMap<>();

    public HttpSessionData(HttpSessionWrapper session, ApplicationContext applicationContext) {
        this.session = session;
        this.applicationContext = applicationContext;
    }

    public Enumeration<String> getAttributeNames() {
        return Collections.enumeration(attributes.keySet());
    }

    public void setAttribute(String name, Object value) {
        if (attributes.containsKey(name)) {
            Object oldValue = attributes.get(name);
            if (!oldValue.equals(value)) {
                // replaced
                attributes.put(name, value);
                applicationContext.sessionAttributeReplaced(session, name, value);
            }
        } else {
            // add
            attributes.put(name, value);
            applicationContext.sessionAttributeAdded(session, name, value);
        }
    }

    public void removeAttribute(String name) {
        if (attributes.containsKey(name)) {
            // remove
            Object value = attributes.get(name);
            attributes.remove(name);
            applicationContext.sessionAttributeRemoved(session, name, value);
        }
    }

    public Object getAttribute(String name) {
        return attributes.get(name);
    }

    public String[] getValueNames() {
        return attributes.keySet().toArray(new String[0]);
    }
}
