package com.github.ljl.jerrymouse.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @program: jerry-mouse-round2
 * @description:
 * @author: ljl
 * @create: 2024-07-09 11:18
 **/

public class TimeUtils {
    private static Logger logger = LoggerFactory.getLogger(TimeUtils.class);
    public static String format(long time) {
        return format(time, "EEE, dd MMM yyyy HH:mm:ss 'GMT'", "GMT");
    }
    public static String format(long time, String pattern) {
        return format(time, pattern, "GMT");
    }
    public static String format(long time, String pattern, String timeZone) {
        Set<String> availableTimeZones = Arrays.stream(TimeZone.getAvailableIDs()).collect(Collectors.toSet());
        if (!availableTimeZones.contains(timeZone)) {
            logger.error("invalid timeZone: " + timeZone);
            throw new IllegalStateException("invalid timeZone: " + timeZone);
        }
        Date date = new Date(time);
        SimpleDateFormat sdf = new SimpleDateFormat(pattern, Locale.US);
        sdf.setTimeZone(TimeZone.getTimeZone(timeZone));
        return sdf.format(date);
    }
}
