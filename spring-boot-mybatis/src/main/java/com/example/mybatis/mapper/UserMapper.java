package com.example.mybatis.mapper;

import com.example.mybatis.model.User;

import java.util.List;

/**
 * @author jingLv
 * @date 2020/09/24
 */
public interface UserMapper {
    /**
     * 获取所有的用户数据
     *
     * @return 返回所有用户列表
     */
    List<User> getAllUsers();

    /**
     * 根据id查询单条用户数据
     *
     * @param id 传入id
     * @return 返回用户数据
     */
    User getOne(Long id);

    /**
     * 新增用户
     *
     * @param user 用户信息
     */
    void insertUser(User user);

    /**
     * 修改用户信息
     *
     * @param user 用户信息
     */
    void updateUser(User user);

    /**
     * 根据id删除用户信息
     *
     * @param id 表的主键
     */
    void deleteUser(Long id);
}
