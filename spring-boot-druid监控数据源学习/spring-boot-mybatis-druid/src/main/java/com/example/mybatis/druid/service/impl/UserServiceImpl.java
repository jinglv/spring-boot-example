package com.example.mybatis.druid.service.impl;

import com.example.mybatis.druid.entity.User;
import com.example.mybatis.druid.mapper.UserMapper;
import com.example.mybatis.druid.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author jingLv
 * @date 2021/01/26
 */
@Service
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;

    public UserServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    /**
     * 查询所有用户信息
     *
     * @return 返回用户信息
     */
    @Override
    public List<User> findAllUsers() {
        return userMapper.getAllUsers();
    }
}
