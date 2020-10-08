package com.example.shiro.dao;

import com.example.shiro.entity.User;

/**
 * @author jinglv
 * @date 2020/10/08
 */
public interface UserDAO {

    /**
     * 用户信息保存
     *
     * @param user 用户信息
     */
    void save(User user);

    /**
     * 根据用户名查询用户信息
     *
     * @param username 用户名
     * @return 返回用户信息
     */
    User findByUserName(String username);
}
