package com.github.ljl.jerrymouse.exception;

/**
 * @program: jerry-mouse-round2
 * @description:
 * @author: ljl
 * @create: 2024-07-05 09:32
 **/

public class ThreadPoolException extends RuntimeException {
    public ThreadPoolException(String message) {
        super(message);
    }
    public ThreadPoolException(String message, Throwable throwable) {
        super(message, throwable);
    }
    public ThreadPoolException(Throwable throwable) {
        super(throwable);
    }
}
