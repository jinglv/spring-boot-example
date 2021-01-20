package com.example.jwt.dao;

import com.example.jwt.entity.User;

/**
 * @author jingLv
 * @date 2020/09/30
 */
public interface UserDAO {
    /**
     * 登录
     *
     * @param user 用户信息
     * @return 登录结果
     */
    User login(User user);
}
