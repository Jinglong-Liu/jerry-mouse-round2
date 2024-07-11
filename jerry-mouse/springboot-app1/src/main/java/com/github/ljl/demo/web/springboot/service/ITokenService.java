package com.github.ljl.demo.web.springboot.service;

public interface ITokenService {

    String generateToken(String value);

    /**
     *
     * @param token token串
     * @return 合法，返回对应的userId, 非法，返回null
     */
    String validateToken(String token);

    void invalidateToken(String token);

    /**
     * @param token 旧token
     * @return 刷新时间后的token
     */
    String refreshToken(String token);
}
