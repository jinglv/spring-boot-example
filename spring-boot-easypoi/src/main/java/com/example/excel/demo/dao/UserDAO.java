package com.example.excel.demo.dao;

import com.example.excel.demo.entity.User;

import java.util.List;

/**
 * @author jingLv
 * @date 2021/01/26
 */
public interface UserDAO {

    /**
     * 查询所有用户信息
     *
     * @return 返回用户信息列表
     */
    List<User> findAll();

    /**
     * 保存所有用户信息
     *
     * @param user 用户信息
     * @return 返回结果
     */
    int save(User user);
}
