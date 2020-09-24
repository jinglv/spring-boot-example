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
}
