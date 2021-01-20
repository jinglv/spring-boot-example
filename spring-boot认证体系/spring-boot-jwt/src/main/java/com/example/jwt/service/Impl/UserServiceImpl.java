package com.example.jwt.service.Impl;

import com.example.jwt.dao.UserDAO;
import com.example.jwt.entity.User;
import com.example.jwt.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author jingLv
 * @date 2020/09/30
 */
@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserDAO userDao;

    public UserServiceImpl(UserDAO userDao) {
        this.userDao = userDao;
    }

    @Override
    public User login(User user) {
        // 根据接收用户名和密码查询数据库
        User userDb = userDao.login(user);
        if (userDb != null) {
            return userDb;
        }
        throw new RuntimeException("登录失败--");
    }
}
