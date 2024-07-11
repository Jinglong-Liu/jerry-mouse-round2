package com.github.ljl.jerrymouse.support.servlet;

import lombok.Getter;

import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * @program: jerry-mouse-round2
 * @description:
 * @author: ljl
 * @create: 2024-07-07 16:20
 **/

public class FilterConfigWrapper implements FilterConfig {

    private final String filterName;

    @Getter
    private final String className;

    private final ServletContext servletContext;

    private final Map<String, String> initParameterMap = new HashMap<>();

    public FilterConfigWrapper(String filterName, String className, ServletContext servletContext) {
        this.filterName = filterName;
        this.className = className;
        this.servletContext = servletContext;
    }
    @Override
    public String getFilterName() {
        return filterName;
    }

    @Override
    public ServletContext getServletContext() {
        return servletContext;
    }

    @Override
    public String getInitParameter(String name) {
        return initParameterMap.get(name);
    }

    @Override
    public Enumeration<String> getInitParameterNames() {
        return Collections.enumeration(initParameterMap.keySet());
    }

    public boolean setInitParameter(String name, String value) {
        if (initParameterMap.containsKey(name)) {
            return false;
        }
        initParameterMap.put(name, value);
        return true;
    }

    public Map<String, String> getInitParameters() {
        return initParameterMap;
    }
}
