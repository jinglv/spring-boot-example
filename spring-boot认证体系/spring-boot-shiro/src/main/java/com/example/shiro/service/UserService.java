package com.example.shiro.service;

import com.example.shiro.entity.User;

/**
 * @author jinglv
 * @date 2020/10/08
 */
public interface UserService {
    /**
     * 用户注册
     *
     * @param user 用户信息
     */
    void register(User user);

    /**
     * 根据用户名查询用户信息
     *
     * @param username 用户名
     * @return 用户信息
     */
    User findByUserName(String username);
}
