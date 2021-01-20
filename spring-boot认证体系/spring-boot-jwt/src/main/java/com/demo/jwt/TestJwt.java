package com.demo.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Calendar;
import java.util.HashMap;

/**
 * JWT示例
 *
 * @author jingLv
 * @date 2020/09/29
 */
public class TestJwt {

    public static void main(String[] args) {
        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.SECOND, 90);
        // 1. 生成token
        HashMap<String, Object> map = new HashMap<>();
        String token = JWT.create()
                // header
                .withHeader(map)
                // payload
                .withClaim("userId", 1001)
                .withClaim("username", "admin")
                // 指定过期时间
                .withExpiresAt(instance.getTime())
                // 设置签名，保密，复杂
                .sign(Algorithm.HMAC256("token!Q2W#E$RW"));

        System.out.println(token);

        // 2. 根据令牌和签名解析数据
        // 创建验证对象
        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256("token!Q2W#E$RW")).build();
        DecodedJWT decodedJwt = jwtVerifier.verify(token);

        System.out.println("用户ID: " + decodedJwt.getClaim("userId").asInt());
        System.out.println("用户名: " + decodedJwt.getClaim("username").asString());
        System.out.println("过期时间: " + decodedJwt.getExpiresAt());
    }
}
