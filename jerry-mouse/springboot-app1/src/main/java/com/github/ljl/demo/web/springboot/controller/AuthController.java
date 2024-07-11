package com.github.ljl.demo.web.springboot.controller;

import com.github.ljl.demo.web.springboot.aspect.validate.AuthValidation;
import com.github.ljl.demo.web.springboot.bean.LoginBean;
import com.github.ljl.demo.web.springboot.bean.User;
import com.github.ljl.demo.web.springboot.service.ITokenService;
import com.github.ljl.demo.web.springboot.service.IUserService;
import com.github.ljl.demo.web.springboot.util.ResponseWrapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

/**
 * @program: jerry-mouse-round2
 * @description:
 * @author: ljl
 * @create: 2024-07-10 13:48
 **/

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Resource
    private IUserService userService;

    @PostMapping("/login")
    public ResponseEntity<ResponseWrapper> login(@RequestBody LoginBean loginBean, HttpServletResponse response) {
        ResponseWrapper responseBody = ResponseWrapper.ok();
        User user = userService.login(loginBean, response);
        responseBody.setData(user);
        if (Objects.isNull(user)) {
            responseBody.setMessage("Invalid credentials").setStatus(401);
            return ResponseEntity.status(401).body(responseBody);
        }
        return ResponseEntity.ok().body(responseBody);
    }

    @AuthValidation
    @PostMapping("/logout")
    public ResponseEntity<ResponseWrapper> logout(HttpServletRequest request, HttpServletResponse response) {
        String token = userService.getTokenByRequest(request);
        User user = userService.logout(token);
        ResponseWrapper responseBody = ResponseWrapper.ok().setData(user).setMessage("logout successful " + user.getUsername());
        return ResponseEntity.ok(responseBody);
    }
}
