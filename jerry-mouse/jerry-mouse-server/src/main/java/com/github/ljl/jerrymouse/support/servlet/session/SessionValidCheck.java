package com.github.ljl.jerrymouse.support.servlet.session;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @program: jerry-mouse-round2
 * @description:
 * @author: ljl
 * @create: 2024-07-08 09:22
 **/

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SessionValidCheck {

}
