package com.example.shiro.service.Impl;

import com.example.shiro.dao.UserDAO;
import com.example.shiro.entity.User;
import com.example.shiro.service.UserService;
import com.example.shiro.utils.SaltUtils;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.stereotype.Service;

/**
 * @author jinglv
 * @date 2020/10/08
 */
@Service("userService")
public class UserServiceImpl implements UserService {

    private final UserDAO userDAO;

    public UserServiceImpl(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    /**
     * 开发注册功能
     *
     * @param user 用户信息
     */
    @Override
    public void register(User user) {
        // 处理业务调用DAO
        // 1.生成随机盐
        String salt = SaltUtils.getSalt(8);
        // 2.将随机盐保存到数据
        user.setSalt(salt);
        // 3.明文密码进行 md5+salt+hash散列
        Md5Hash md5Hash = new Md5Hash(user.getPassword(), salt, 1024);
        user.setPassword(md5Hash.toHex());
        // 保存到数据库中
        userDAO.save(user);
    }

    /**
     * 根据用户名查询用户信息实现
     *
     * @param username 用户名
     * @return 返回用户信息
     */
    @Override
    public User findByUserName(String username) {
        return userDAO.findByUserName(username);
    }
}
