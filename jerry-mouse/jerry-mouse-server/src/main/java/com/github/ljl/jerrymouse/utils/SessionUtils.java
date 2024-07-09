package com.github.ljl.jerrymouse.utils;

import java.security.SecureRandom;

/**
 * @program: jerry-mouse-round2
 * @description:
 * @author: ljl
 * @create: 2024-07-08 11:10
 **/

public class SessionUtils {
    private static final SecureRandom random = new SecureRandom();

    public static String generateSessionId() {
        long timestamp = System.currentTimeMillis();
        int rand = random.nextInt();
        String sessionId = Long.toString(timestamp) + Integer.toHexString(rand);
        return sessionId;
    }
}
