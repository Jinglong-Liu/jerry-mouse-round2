package com.github.ljl.demo.web.springboot.service;

import com.github.ljl.demo.web.springboot.bean.LoginBean;
import com.github.ljl.demo.web.springboot.bean.User;
import com.github.ljl.demo.web.springboot.bean.UserAndToken;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @program: jerry-mouse-round2
 * @description:
 * @author: ljl
 * @create: 2024-07-10 14:03
 **/

@Service
public class UserService implements IUserService {

    @Value("${token.header}")
    private String AUTHORIZATION_HEADER;

    /**
     * mock data
     */
    private static final List<User> mockUsers =

            Arrays.stream(new User[]{
                    new User("id001", "username", "password", "desc1"),
                    new User("id002", "username2", "password2", "desc2"),
            }).collect(Collectors.toList());

    @Value("${token.header}")
    private String authorizationHeader;

    @Resource
    private ITokenService tokenService;

    @Override
    public User findUserById(String userId) {
        List<User> users = mockUsers.stream()
                .filter(user -> user.getUserId().equals(userId))
                .collect(Collectors.toList());
        if (!users.isEmpty()) {
            return users.get(0);
        }
        return null;
    }

    /**
     * @param loginBean
     * @return user which is checked, if none checked, return null.
     */
    @Override
    public User login(@NotNull LoginBean loginBean, @NotNull HttpServletResponse response) {
        List<User> users = mockUsers.stream()
                .filter(user -> user.getUsername().equals(loginBean.getUsername()))
                .filter(user ->  user.getPassword().equals(loginBean.getPassword()))
                .collect(Collectors.toList());

        if (users.isEmpty()) {
            return null;
        }

        User user = users.get(0);

        String userId = user.getUserId();
        // token
        String token = tokenService.generateToken(userId);
        // System.out.println("------------token=" + token + "-----------");
        response.setHeader(AUTHORIZATION_HEADER,  token);
        return user;
    }

    @Override
    public User logout(String token) {
        if (Objects.isNull(token)) {
            return null;
        }
        User user = getUserByToken(token);
        if (Objects.nonNull(user)) {
            tokenService.invalidateToken(token);
        }
        return user;
    }

    private User getUserByToken(@NotNull String token) {
        // 要求username唯一
        String userId = tokenService.validateToken(token);
        return findUserById(userId);
    }

    @Override
    public User validateUserByRequestToken(@NotNull HttpServletRequest request) {
        String token = getTokenByRequest(request);
        if (Objects.isNull(token)) {
            return null;
        }

        User user = getUserByToken(token);
        if (Objects.isNull(user)) {
            return null;
        }
        return getUserByToken(token);
    }

    @Override
    public UserAndToken validateAndRefreshToken(@NotNull HttpServletRequest request) {
        String token = getTokenByRequest(request);
        if (Objects.isNull(token)) {
            return null;
        }

        User user = getUserByToken(token);
        if (Objects.isNull(user)) {
            return null;
        }
        String refreshedToken = tokenService.refreshToken(token);

        return new UserAndToken(user, refreshedToken);
    }

    @Override
    public String getTokenByRequest(@NotNull HttpServletRequest request) {
        String prefix = "Bearer ";
        String token = request.getHeader(authorizationHeader);
        if (Objects.isNull(token) || !token.startsWith(prefix)) {
            return null;
        }
        token = token.substring(prefix.length());
        return token;
    }
}
