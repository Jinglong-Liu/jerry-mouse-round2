package com.github.ljl.demo.web.springboot.aspect.validate;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @program: jerry-mouse-round2
 * @description:
 * @author: ljl
 * @create: 2024-07-11 09:07
 **/

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface AuthValidation {
}
