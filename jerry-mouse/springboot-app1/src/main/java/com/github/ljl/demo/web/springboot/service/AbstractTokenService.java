package com.github.ljl.demo.web.springboot.service;

import org.springframework.beans.factory.annotation.Value;

import java.util.Objects;

/**
 * @program: jerry-mouse-round2
 * @description:
 * @author: ljl
 * @create: 2024-07-11 10:01
 **/

public abstract class AbstractTokenService implements ITokenService {

    @Value("${token.secret}")
    protected String secret;

    @Value("${token.expireTime}")
    protected Long expiration;


    @Override
    public abstract String generateToken(String value);

    @Override
    public String validateToken(String token) {
        return getUserIdFromToken(token);
    }

    /**
     * @param token
     */

    @Override
    public abstract void invalidateToken(String token);

    @Override
    public String refreshToken(String token) {
        String userId = getUserIdFromToken(token);
        if (Objects.isNull(userId)) {
            return null;
        }
        // 删除旧的token
        invalidateToken(token);
        // 实际上就是重写生成一个token
        return generateToken(userId);
    }

    protected abstract String getUserIdFromToken(String token);
}
