package com.github.ljl.jerrymouse.support.servlet.manager;

import javax.servlet.http.HttpServlet;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @program: jerry-mouse-round2
 * @description:
 * @author: ljl
 * @create: 2024-07-05 19:47
 **/

public class ServletManager {
    private Map<String, HttpServlet> servletMap = new HashMap<>();

    public HttpServlet getServlet(String url) {
        List<String[]> pairs = findKey(url);
        if (!pairs.isEmpty()) {
            String[] pair = pairs.get(0);
            return servletMap.get(pair[0]);
        } else {
            return null;
        }
    }
    public Object[] getServletAndPath(String url) {
        List<String[]> pairs = findKey(url);
        if (!pairs.isEmpty()) {
            String[] pair = pairs.get(0);
            return new Object[]{servletMap.get(pair[0]), pair[1]};
        } else {
            return new Object[]{null, null};
        }
    }

    public void register(String urlPattern, HttpServlet servlet) {
        servletMap.put(urlPattern, servlet);
    }

    private List<String[]> findKey(String url) {
        List<String[]> list = new ArrayList<>();
        for (String urlPattern: servletMap.keySet()) {
            String servletPath = matchedServletPath(urlPattern, url.trim());
            if (Objects.nonNull(servletPath)) {
                list.add(new String[]{urlPattern, servletPath});
            }
        }
        return list;
    }

    private String getDefaultPath(String url) {
        // 已经在对应的context, 因此根据url求即可
        int index = url.indexOf('/', 1);
        return url.substring(0, index + 1);
    }
    private String matchedServletPath(String urlPattern, String url) {
        if (urlPattern.equals(url)) {
            return url; // 精确匹配, 返回整个
        }
        if (urlPattern.endsWith("/*")) {
            String prefix = urlPattern.substring(0, urlPattern.length() - 2);
            if(url.startsWith(prefix)) { // 前缀匹配
                return prefix;
            }
        }
        if (urlPattern.startsWith("*.")) {
            String suffix = urlPattern.substring(1);
            if(url.endsWith(suffix)) {
                int start = url.lastIndexOf('/');
                return url.substring(start);
            }
        }
        if (urlPattern.equals(getDefaultPath(url))) {
            // 整个app
            return "";
        }
        return null;
    }
}
