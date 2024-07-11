package com.github.ljl.demo.web.springboot.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @program: jerry-mouse-round2
 * @description:
 * @author: ljl
 * @create: 2024-07-11 10:18
 **/

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserAndToken {
    private User user;
    private String token;
}
