package com.github.ljl.jerrymouse.support.servlet.response;

import lombok.Data;

import java.util.Map;

/**
 * @program: jerry-mouse-round2
 * @description:
 * @author: ljl
 * @create: 2024-07-05 18:02
 **/

@Data
public class ResponseData {
    private Map<String, String> headers;
}
