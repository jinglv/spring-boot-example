package com.example.jwt.service;

import com.example.jwt.entity.User;

/**
 * @author jingLv
 * @date 2020/09/30
 */
public interface UserService {
    /**
     * 登录接口
     *
     * @param user 用户信息
     * @return 返回登录结果
     */
    User login(User user);
}
