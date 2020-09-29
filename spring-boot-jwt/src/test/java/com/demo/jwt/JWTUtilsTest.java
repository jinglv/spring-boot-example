package com.demo.jwt;

import com.auth0.jwt.interfaces.DecodedJWT;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * @author jingLv
 * @date 2020/09/29
 */
class JWTUtilsTest {

    @Test
    void getToken() {
        Map<String, String> map = new HashMap<>();
        map.put("userId", "001");
        map.put("userName", "admin");
        System.out.println(JWTUtils.getToken(map));
    }

    @Test
    void getTokenInfo() {
        // 验证token
        JWTUtils.verify(token());
        // 获取token信息
        DecodedJWT decodedJwt = JWTUtils.getTokenInfo(token());
        System.out.println("用户ID: " + decodedJwt.getClaim("userId").asString());
        System.out.println("用户名: " + decodedJwt.getClaim("userName").asString());
        System.out.println("过期时间: " + decodedJwt.getExpiresAt());
    }

    private String token() {
        Map<String, String> map = new HashMap<>();
        map.put("userId", "001");
        map.put("userName", "admin");
        return JWTUtils.getToken(map);
    }
}