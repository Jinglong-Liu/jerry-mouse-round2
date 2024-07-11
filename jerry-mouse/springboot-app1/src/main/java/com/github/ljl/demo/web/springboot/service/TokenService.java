package com.github.ljl.demo.web.springboot.service;

import com.github.ljl.demo.web.springboot.dao.RedisUtil;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @program: jerry-mouse-round2
 * @description:
 * @author: ljl
 * @create: 2024-07-10 16:57
 **/

@Service
public class TokenService extends AbstractTokenService {

    private static final String REDIS_TOKEN_PREFIX = "token:";

    @Resource
    private RedisUtil redisUtil;

    @Override
    public String generateToken(String value) {
        Date expirationDate = new Date(System.currentTimeMillis() + expiration * 1000);
        String token = Jwts.builder()
                .setSubject(value)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
        redisUtil.setWithExpiry(REDIS_TOKEN_PREFIX + token, value, expiration, TimeUnit.SECONDS);
        return token;
    }

    @Override
    protected String getUserIdFromToken(String token) {
        return (String) redisUtil.get(REDIS_TOKEN_PREFIX + token);
    }

    @Override
    public void invalidateToken(@NotNull String token) {
        // 从 Redis 中删除 token
        redisUtil.delete(REDIS_TOKEN_PREFIX + token);
    }
}
