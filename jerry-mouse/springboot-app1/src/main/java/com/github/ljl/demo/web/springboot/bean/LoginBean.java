package com.github.ljl.demo.web.springboot.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @program: jerry-mouse-round2
 * @description:
 * @author: ljl
 * @create: 2024-07-10 13:56
 **/

@AllArgsConstructor
@NoArgsConstructor
@Data
public class LoginBean {
    private String username;
    private String password;
}
