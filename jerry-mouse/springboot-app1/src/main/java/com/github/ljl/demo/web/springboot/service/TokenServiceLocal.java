package com.github.ljl.demo.web.springboot.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @program: jerry-mouse-round2
 * @description:
 * @author: ljl
 * @create: 2024-07-10 20:04
 **/

// 不生效
// @Service
public class TokenServiceLocal extends AbstractTokenService {

    @Override
    public String generateToken(String value) {
        Map<String, Object> claims = new HashMap<>(2);
        claims.put("sub", value); // setSubject
        claims.put("created", new Date());

        Date expirationDate = new Date(System.currentTimeMillis() + expiration * 1000);
        String token = Jwts.builder()
                .setClaims(claims)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();

        return token;
    }

    @Override
    public String validateToken(String token) {
        return getUserIdFromToken(token);
    }

    @Override
    public void invalidateToken(String token) {
        System.out.println("local invalidateToken can do nothing");
        // 本地操作不了，因为token本身就含有时间，除非把token字符串换了，那样的话，就不是这个token了
        // 考虑加黑名单白名单
    }


    private Claims getClaimsFromToken(String token) {
        Claims claims;
        try {
            claims = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            claims = null;
        }
        return claims;
    }

    private boolean isExpired(Claims claims){
        Date expiration = claims.getExpiration();
        return expiration.before(new Date());
    }

    protected String getUserIdFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        if (Objects.isNull(claims)) {
            return null;
        }
        /**
         * 超时
         */
        if (isExpired(claims)) {
            return null;
        }
        return claims.getSubject();
    }
}
