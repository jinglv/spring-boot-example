package com.example.mybatis.druid.service;

import com.example.mybatis.druid.entity.User;

import java.util.List;

/**
 * @author jingLv
 * @date 2021/01/26
 */
public interface UserService {

    /**
     * 查询所有用户信息
     *
     * @return 返回用户信息
     */
    List<User> findAllUsers();
}
