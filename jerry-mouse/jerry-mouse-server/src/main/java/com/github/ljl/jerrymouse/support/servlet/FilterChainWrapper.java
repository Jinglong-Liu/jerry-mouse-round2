package com.github.ljl.jerrymouse.support.servlet;

import javax.servlet.*;
import javax.servlet.http.HttpServlet;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

/**
 * @program: jerry-mouse-round2
 * @description:
 * @author: ljl
 * @create: 2024-07-07 15:57
 **/

public class FilterChainWrapper implements FilterChain {
    private final List<Filter> filterList;

    private final Servlet servlet;

    private int index;

    public FilterChainWrapper(List<Filter> filterList, Servlet servlet) {
        this.filterList = filterList;
        this.servlet = servlet;
    }
    @Override
    public void doFilter(ServletRequest request, ServletResponse response) throws IOException, ServletException {
        if (index < filterList.size()) {
            Filter filter = filterList.get(index);
            index++;
            filter.doFilter(request, response, this);
        } else if (index == filterList.size()){
            if (Objects.nonNull(servlet)) {
                servlet.service(request, response);
            }
        }
    }
}
