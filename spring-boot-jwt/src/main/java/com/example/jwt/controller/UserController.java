package com.example.jwt.controller;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.jwt.entity.User;
import com.example.jwt.service.UserService;
import com.example.jwt.utils.JWTUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @author jingLv
 * @date 2020/09/30
 */
@RestController
@Slf4j
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/user/login")
    public Map<String, Object> login(User user) {
        log.info("用户名：{}", user.getUserName());
        log.info("密码：{}", user.getPassWord());

        Map<String, Object> result = new HashMap<>();
        try {
            User userDB = userService.login(user);
            Map<String, String> payload = new HashMap<>();
            payload.put("id", userDB.getId());
            payload.put("userName", userDB.getUserName());
            // 生成JWT令牌
            String token = JWTUtils.getToken(payload);
            result.put("state", true);
            result.put("message", "登录成功!!!");
            result.put("token", token);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("state", false);
            result.put("message", e.getMessage());
        }
        return result;
    }

    @PostMapping("/user/test")
    public Map<String, Object> test(HttpServletRequest request) {
        // 处理自己的业务逻辑
        Map<String, Object> result = new HashMap<>();
        result.put("msg", "请求成功!!!");
        result.put("state", true);
        // 获取认证信息
        String token = request.getHeader("token");
        DecodedJWT verify = JWTUtils.verify(token);
        log.info("用户Id：{}", verify.getClaim("id").asString());
        log.info("用户名：{}", verify.getClaim("userName").asString());
        return result;
    }
}
