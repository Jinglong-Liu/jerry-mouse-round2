package com.github.ljl.demo.web.springboot.aspect.validate;

import com.github.ljl.demo.web.springboot.bean.User;
import com.github.ljl.demo.web.springboot.service.IUserService;
import com.github.ljl.demo.web.springboot.util.ResponseWrapper;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * @program: jerry-mouse-round2
 * @description:
 * @author: ljl
 * @create: 2024-07-11 09:06
 **/

@Aspect
@Component
public class AuthValidationAspect {

    @Resource
    private IUserService userService;

    @Around("@annotation(AuthValidation)")
    public Object validateMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        // 获取方法参数
        Object[] args = joinPoint.getArgs();

        // 在这里添加校验逻辑
        boolean isValid = validate(args);

        if (!isValid) {
            // 校验失败，返回预定义的结果
            ResponseWrapper responseBody = ResponseWrapper.unAuthorized().setMessage("Unauthorized, checked by aop");
            return ResponseEntity.status(401).body(responseBody);
        }

        // 校验通过，继续执行目标方法
        return joinPoint.proceed();
    }

    // 自定义校验逻辑
    private boolean validate(Object[] args) {
        HttpServletRequest request = null;
        for (Object arg: args) {
            if (arg instanceof  HttpServletRequest) {
                request = (HttpServletRequest) arg;
                break;
            }
        }
        if (Objects.isNull(request)) {
            System.out.println("controller need request arg to validate");
            return false;
        }
        return validateRequest(request);
    }
    private boolean validateRequest(HttpServletRequest request) {
        User user = userService.validateUserByRequestToken(request);
        return Objects.nonNull(user);
    }
}
