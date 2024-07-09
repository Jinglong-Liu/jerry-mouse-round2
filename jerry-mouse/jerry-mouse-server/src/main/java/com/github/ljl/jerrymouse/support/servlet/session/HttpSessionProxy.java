package com.github.ljl.jerrymouse.support.servlet.session;
;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @program: jerry-mouse-round2
 * @description: HttpSessionWrapper的代理对象
 *               带有 @SessionValidCheck 的注解的 public 方法，进行session合法校验拦截
 * @author: ljl
 * @create: 2024-07-08 09:18
 **/

public class HttpSessionProxy implements InvocationHandler {
    private Logger logger = LoggerFactory.getLogger(HttpSessionProxy.class);

    private final HttpSessionWrapper sessionWrapper;

    public HttpSessionProxy(HttpSessionWrapper sessionWrapper) {
        this.sessionWrapper = sessionWrapper;
    }

    /**
     * 所有方法都被拦截，那么invoke实际上全部是交给sessionWrapper这个类执行即可
     * @param proxy
     * @param method
     * @param args
     * @return
     * @throws Throwable
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        HttpSessionWrapper session = sessionWrapper;

        if (method.isAnnotationPresent(SessionValidCheck.class)) {
            // 合法检查
            if (!session.isValid()) {
                logger.warn("Session is invalid, maybe expired. id={}", sessionWrapper.getId());
                throw new IllegalStateException("Session is invalid.");
            }
        }
        // 调用实际传入的 HttpSession 对象的方法
        Object result = method.invoke(session, args);
        return result;
    }
}
