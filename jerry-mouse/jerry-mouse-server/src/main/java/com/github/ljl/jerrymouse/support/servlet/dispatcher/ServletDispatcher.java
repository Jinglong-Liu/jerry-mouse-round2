package com.github.ljl.jerrymouse.support.servlet.dispatcher;

import com.github.ljl.jerrymouse.support.context.ApplicationContext;
import com.github.ljl.jerrymouse.support.servlet.response.JerryMouseResponse;
import com.github.ljl.jerrymouse.utils.HttpUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @program: jerry-mouse-round2
 * @description:
 * @author: ljl
 * @create: 2024-07-05 20:10
 **/

public class ServletDispatcher implements IDispatcher {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void dispatch(RequestDispatcherContext context) {
        HttpServletRequest request = context.getRequest();
        JerryMouseResponse response = (JerryMouseResponse) context.getResponse();
        //TODO: use uriPattern
        ApplicationContext applicationContext = (ApplicationContext) request.getServletContext();
        HttpServlet httpServlet = applicationContext.getServletByURI(request.getRequestURI());
        try {
            httpServlet.service(request, response);
        } catch (NullPointerException e) {
            logger.error("Servlet not found! uri = {}", request.getRequestURI());
            response.getSockerWriter().write(HttpUtils.http404Resp());
        } catch (ServletException | IOException e) {
            logger.error("Server meet error when do servlet Service");
            e.printStackTrace();
            response.getSockerWriter().write(HttpUtils.http500Resp(e));
        }
    }
}
