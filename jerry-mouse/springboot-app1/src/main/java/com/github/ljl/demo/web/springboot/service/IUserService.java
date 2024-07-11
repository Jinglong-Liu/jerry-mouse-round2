package com.github.ljl.demo.web.springboot.service;

import com.github.ljl.demo.web.springboot.bean.LoginBean;
import com.github.ljl.demo.web.springboot.bean.User;
import com.github.ljl.demo.web.springboot.bean.UserAndToken;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @program: jerry-mouse-round2
 * @description:
 * @author: ljl
 * @create: 2024-07-10 14:03
 **/

public interface IUserService {
    User findUserById(String userId);

    User login(LoginBean loginBean, HttpServletResponse response);

    User logout(String token);

    User validateUserByRequestToken(HttpServletRequest request);

    UserAndToken validateAndRefreshToken(HttpServletRequest request);

    String getTokenByRequest(HttpServletRequest request);
}
