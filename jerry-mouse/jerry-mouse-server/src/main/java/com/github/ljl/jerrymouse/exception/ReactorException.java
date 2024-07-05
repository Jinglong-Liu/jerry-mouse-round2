package com.github.ljl.jerrymouse.exception;

/**
 * @program: jerry-mouse-round2
 * @description:
 * @author: ljl
 * @create: 2024-07-05 12:53
 **/

public class ReactorException extends RuntimeException{
    public ReactorException(String message) {
        super(message);
    }
    public ReactorException(String message, Throwable throwable) {
        super(message, throwable);
    }
    public ReactorException(Throwable throwable) {
        super(throwable);
    }
}
