package com.github.ljl.demo.web.springboot.controller;

import com.github.ljl.demo.web.springboot.aspect.validate.AuthValidation;
import com.github.ljl.demo.web.springboot.bean.User;
import com.github.ljl.demo.web.springboot.bean.UserAndToken;
import com.github.ljl.demo.web.springboot.service.IUserService;
import com.github.ljl.demo.web.springboot.util.ResponseWrapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
 * @create: 2024-07-10 16:30
 **/

@RestController
@RequestMapping("/auth")
public class WelcomeController {

    @Value("${token.header}")
    private String AUTHORIZATION_HEADER;

    @Resource
    private IUserService userService;

    @GetMapping("/welcome")
    public ResponseEntity<ResponseWrapper> welcome(HttpServletRequest request) {

        User user = userService.validateUserByRequestToken(request);
        if (Objects.isNull(user)) {
            ResponseWrapper responseBody = ResponseWrapper.unAuthorized();
            return ResponseEntity.status(401).body(responseBody);
        }

        ResponseWrapper responseBody =
                ResponseWrapper.ok()
                        .setMessage("Welcome " + user.getUsername())
                        .setData(user);

        return ResponseEntity.ok().body(responseBody);
    }

    @AuthValidation
    @GetMapping("/refresh")
    public ResponseEntity<ResponseWrapper> refresh(HttpServletRequest request, HttpServletResponse response) {

        final UserAndToken bean = userService.validateAndRefreshToken(request);

        // 返回浏览器新的token
        response.setHeader(AUTHORIZATION_HEADER,  bean.getToken());

        ResponseWrapper responseBody =
                ResponseWrapper.ok()
                        .setMessage("token refreshed " + bean.getUser().getUsername())
                        .setData(bean.getUser());

        return ResponseEntity.ok().body(responseBody);
    }
}
