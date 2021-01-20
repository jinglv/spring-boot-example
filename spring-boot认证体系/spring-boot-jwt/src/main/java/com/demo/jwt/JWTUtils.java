package com.demo.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Calendar;
import java.util.Map;

/**
 * JWT工具类的封装
 *
 * @author jingLv
 * @date 2020/09/29
 */
public class JWTUtils {

    private static final String SING = "!Q@W3e4r";

    /**
     * 生成token
     * header.payload.signature
     *
     * @param map header.payload.signature
     * @return 返回token
     */
    public static String getToken(Map<String, String> map) {
        // 默认过期时间7天
        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.DATE, 7);
        // 生成令牌
        JWTCreator.Builder builder = JWT.create();
        map.forEach(builder::withClaim);
        // 指定过期时间和设置签名
        return builder.withExpiresAt(instance.getTime()).sign(Algorithm.HMAC256(SING));
    }

    /**
     * 验证token合法性
     *
     * @param token 需要验证的token
     */
    public static void verify(String token) {
        JWT.require(Algorithm.HMAC256(SING)).build().verify(token);
    }

    /**
     * 获取token信息
     *
     * @param token 生成的token
     * @return 返回已获取的信息
     */
    public static DecodedJWT getTokenInfo(String token) {
        return JWT.require(Algorithm.HMAC256(SING)).build().verify(token);
    }
}
