package com.github.ljl.demo.web.springboot.bean;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @program: jerry-mouse-round2
 * @description:
 * @author: ljl
 * @create: 2024-07-10 14:04
 **/

@AllArgsConstructor
@Data
public class User {
    private String userId;
    private String username;
    private String password;
    private String desc;
}
