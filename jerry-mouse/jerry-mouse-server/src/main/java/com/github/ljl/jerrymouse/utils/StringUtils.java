package com.github.ljl.jerrymouse.utils;

import java.util.Objects;

/**
 * @program: jerry-mouse-round2
 * @description:
 * @author: ljl
 * @create: 2024-07-06 11:25
 **/

public class StringUtils {
    public static final String EMPTY = "";

    public static boolean isEmpty(String s) {
        return Objects.isNull(s) || s.length() == 0;
    }

    /**
     * 是否为空-进行 trim 之后
     *
     * @param string 原始字符串
     * @return 是否为空
     */
    public static boolean isEmptyTrim(final String string) {
        if (isEmpty(string)) {
            return true;
        }

        return isEmpty(string.trim());
    }
}
