package com.github.ljl.jerrymouse.support.servlet.dispatcher;

import com.github.ljl.jerrymouse.support.servlet.response.JerryMouseResponse;
import com.github.ljl.jerrymouse.utils.HttpUtils;


/**
 * @program: jerry-mouse-round2
 * @description:
 * @author: ljl
 * @create: 2024-07-05 20:09
 **/

public class EmptyDispatcher implements IDispatcher {

    @Override
    public void dispatch(RequestDispatcherContext context) {
        JerryMouseResponse response = (JerryMouseResponse) context.getResponse();
        response.getSockerWriter().write(HttpUtils.http404Resp());
    }
}
