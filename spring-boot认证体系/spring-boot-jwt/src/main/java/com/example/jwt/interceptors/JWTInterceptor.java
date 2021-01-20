package com.example.jwt.interceptors;

import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.example.jwt.utils.JWTUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * 拦截器
 *
 * @author jingLv
 * @date 2020/09/30
 */
public class JWTInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 获取请求头中的令牌
        String token = request.getHeader("token");
        Map<String, Object> map = new HashMap<>();
        try {
            // 验证token有效性
            JWTUtils.verify(token);
            //放行请求
            return true;
        } catch (TokenExpiredException e) {
            e.printStackTrace();
            map.put("msg", "token已经过期!!!");
        } catch (SignatureVerificationException e) {
            e.printStackTrace();
            map.put("msg", "签名错误!!!");
        } catch (AlgorithmMismatchException e) {
            e.printStackTrace();
            map.put("msg", "加密算法不匹配!!!");
        } catch (Exception e) {
            e.printStackTrace();
            map.put("msg", "无效token!!!");
        }
        // 设置状态
        map.put("state", false);

        // 将Map转为json，集成了jackson，直接使用
        String json = new ObjectMapper().writeValueAsString(map);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().println(json);
        return false;
    }
}
